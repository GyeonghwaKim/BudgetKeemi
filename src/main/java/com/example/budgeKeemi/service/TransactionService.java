package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.type.IsActive;
import com.example.budgeKeemi.dto.req.ReqTransaction;
import com.example.budgeKeemi.dto.resp.*;
import com.example.budgeKeemi.domain.entity.Account;
import com.example.budgeKeemi.domain.entity.Category;
import com.example.budgeKeemi.domain.type.CategoryStatus;
import com.example.budgeKeemi.domain.entity.Transaction;
import com.example.budgeKeemi.exception.excep.InsufficientBalanceException;
import com.example.budgeKeemi.exception.excep.UnauthorizedException;
import com.example.budgeKeemi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository repository;

    private final AccountService accountService;
    private final CategoryService categoryService;

    public RespTransaction createTransaction(ReqTransaction reqTransaction,String username) {
        
        Account account=accountService.getAccountById(reqTransaction.getAccountId());
        Category category = categoryService.getCategoryByCategoryId(reqTransaction.getCategoryId());

        validationAuthorization(username, account, category,"작성 권한이 없습니다");

        //잔액 부족 시나리오
        if(category.getStatus()==CategoryStatus.INCOME){
            account.adjustBalance(reqTransaction.getAmount());
        }else{
            if(account.getBalance()<reqTransaction.getAmount()){
                throw new InsufficientBalanceException("잔액이 부족합니다.");
            }
            account.adjustBalance(-reqTransaction.getAmount());
        }

        Transaction transaction= ReqTransaction.toEntity(reqTransaction);

        transaction.addAccount(account);
        transaction.addCategory(category);

        Transaction saveTransaction = this.repository.save(transaction);

        RespTransaction respTransaction = RespTransaction.toDto(saveTransaction);

        return respTransaction;
    }

    private static void validationAuthorization(String username, Account account, Category category,String message) {
        if(!(account.getMember().getUsername().equals(username) && category.getMember().getUsername().equals(username))){
            throw new UnauthorizedException(message);
        }
    }

    public List<RespTransaction> getTransactionsByUsername(String username) {

        List<Long> accountIds = getAccountIds(username);

        List<Transaction> transactions=repository.findAllByAccountIdInOrderByTransacDateDesc(accountIds);

        List<RespTransaction> respTransactions = transactions
                .stream()
                .map(RespTransaction::toDto)
                .toList();
        return respTransactions;
    }

    private List<Long> getAccountIds(String username) {
        List<Long> accountIds = accountService.getAccountsByUsername(username)
                .stream()
                .map(RespAccount::getId)
                .toList();
        return accountIds;
    }


    public RespTransaction updateTransaction(Long id, ReqTransaction reqTransaction) {

        Optional<Transaction> _transaction = repository.findById(id);

        if(_transaction.isPresent()){
            Transaction transaction = _transaction.get();

            log.info("amount = {}", reqTransaction.getAmount());
            log.info("desc = {}", reqTransaction.getDescription());

            transaction.updateAmount(reqTransaction.getAmount());
            transaction.updateDescription(reqTransaction.getDescription());

            Transaction updateTransaction = repository.save(transaction);

            int amount = updateTransaction.getAmount();
            String description = updateTransaction.getDescription();
            log.info("update amount = {}",amount);
            log.info("update description = {}",description);

            return RespTransaction.toDto(updateTransaction);
        }else{
            return null;
        }

    }

    public boolean cancelTransaction(Long id, String username) {
        Optional<Transaction> _transaction = repository.findById(id);

        if(_transaction.isPresent()){
            Transaction transaction = _transaction.get();

            Account account = transaction.getAccount();
            Category category = transaction.getCategory();
            //소유자 검증
            validationAuthorization(username, account, category,"취소 권한이 없습니다");
            //지출 및 활성화 거래내역 취소
            if(category.getStatus()==CategoryStatus.EXPENSE && transaction.getActive()== IsActive.Y){
                transaction.changeActive(IsActive.N);
                //잔액 복구
                account.adjustBalance(transaction.getAmount());
                repository.save(transaction);
                return true;
            }

            return false;
        }else{
            return false;
        }
    }

    //한달 거래내역 총 합계 출력
    public MonthlySummary getMonthlySummary(YearMonth yearMonth,String username) {

        List<Long> accountIds = getAccountIds(username);

        List<Transaction> transactionList = getMonthlyTransactions(yearMonth,accountIds);

        //TODO 수입, 지출 메서드로 분리하자
        int totalIncome = transactionList
                .stream()
                .filter(transaction -> transaction.getCategory().getStatus()== CategoryStatus.INCOME)
                .mapToInt(Transaction::getAmount)
                .sum();

        int totalExpense = transactionList
                .stream()
                .filter(transaction -> transaction.getCategory().getStatus()== CategoryStatus.EXPENSE)
                .mapToInt(Transaction::getAmount)
                .sum();

        MonthlySummary monthlySummary = MonthlySummary.builder()
                .yearMonth(yearMonth)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .build();

        return monthlySummary;

    }

    private List<Transaction> getMonthlyTransactions(YearMonth yearMonth,List<Long> accountIds) {

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23,59,59);

        List<Transaction> transactionList=repository.findByDateAndAccountId(startDate,endDate,accountIds);

        return transactionList;
    }

    //캘린더에 쓰일 한달 거래 내역 리스트
    public List<DailySummary> getDaySummary(YearMonth yearMonth, String username) {

        List<Long> accountIds = getAccountIds(username);
        List<Transaction> transactionList =getMonthlyTransactions(yearMonth,accountIds);

        Map<LocalDate,Integer> incomeMap=new HashMap<>();
        Map<LocalDate,Integer> expenseMap=new HashMap<>();

        //지출, 수입 분류
        for (Transaction transaction : transactionList) {
            LocalDate key = transaction.getTransacDate().toLocalDate();
            if(transaction.getCategory().getStatus()==CategoryStatus.INCOME) {
                incomeMap.put(key, incomeMap.getOrDefault(key,0)+transaction.getAmount());
            }else{
                expenseMap.put(key, expenseMap.getOrDefault(key,0)+transaction.getAmount());
            }
        }

        List<DailySummary> dailySummaries=new ArrayList<>();
//수입
        for (Map.Entry<LocalDate, Integer> localDateIntegerEntry : incomeMap.entrySet()) {
            dailySummaries.add(DailySummary.builder()
                    .date(localDateIntegerEntry.getKey())
                    .status(CategoryStatus.INCOME)
                    .total(localDateIntegerEntry.getValue())
                    .build());
        }
//지출
        for (Map.Entry<LocalDate,Integer> localDateIntegerEntry : expenseMap.entrySet()) {
            dailySummaries.add(DailySummary.builder()
                    .date(localDateIntegerEntry.getKey())
                    .status(CategoryStatus.EXPENSE)
                    .total(localDateIntegerEntry.getValue())
                    .build());
        }

        return dailySummaries;
    }

    public List<Transaction> getTransactionsByCategoryId(Long categoryId) {
        List<Transaction> transactions = repository.findByCategoryId(categoryId);
        return transactions;
    }
//월별 지출 그래프
    public List<ExpenseGraph> getExpenseGraph(String startDate, String endDate,String username) {

        List<Long> accountIds = getAccountIds(username);

        List<Transaction> transactions =repository.findByDateAndAccountId(LocalDateTime.parse(startDate+"T00:00:00"), LocalDateTime.parse(endDate+"T23:59:59"), accountIds);

        Map<String,Integer> expenseMap=new HashMap<>();
        for (Transaction transaction : transactions) {

            String key = transaction.getCategory().getName();
            if(transaction.getCategory().getStatus()==CategoryStatus.EXPENSE) {
                expenseMap.put(key, expenseMap.getOrDefault(key,0)+transaction.getAmount());
            }

        }
        List<ExpenseGraph> expenseGraphs=new ArrayList<>();
        for (Map.Entry<String, Integer> entry : expenseMap.entrySet()) {
            expenseGraphs.add(ExpenseGraph.toDto(entry.getKey(),entry.getValue()));
        }

        return expenseGraphs;
    }
}
