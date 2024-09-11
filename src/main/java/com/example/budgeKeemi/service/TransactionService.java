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

    public RespTransaction createTransaction(ReqTransaction reqTransaction, String username) {

        Account account = accountService.getAccountById(reqTransaction.getAccountId());
        Category category = categoryService.getCategoryByCategoryId(reqTransaction.getCategoryId());
        //소유자 검증
        validationAuthorization(username, account, category, "작성 권한이 없습니다");

        //잔액 부족 시나리오
        InsufficientBalance(reqTransaction, category, account);

        Transaction transaction = ReqTransaction.toEntity(reqTransaction);
        transaction.addAccount(account);
        transaction.addCategory(category);

        Transaction saveTransaction = this.repository.save(transaction);

        return RespTransaction.toDto(saveTransaction);
    }




    public List<RespTransaction> getTransactionsByUsername(String username) {

        List<Long> accountIds = getAccountIds(username);

        List<Transaction> transactions = repository.findAllByAccountIdInOrderByTransacDateDesc(accountIds);

        return transactions.stream()
                .map(RespTransaction::toDto)
                .toList();
    }


    public boolean cancelTransaction(Long id, String username) {

        Optional<Transaction> _transaction = repository.findById(id);

        if (_transaction.isPresent()) {
            Transaction transaction = _transaction.get();

            Account account = transaction.getAccount();
            Category category = transaction.getCategory();
            //소유자 검증
            validationAuthorization(username, account, category, "취소 권한이 없습니다");

            //지출 및 활성화 거래내역 취소
            if (category.getStatus() == CategoryStatus.EXPENSE && transaction.getActive() == IsActive.Y) {
                //거래내역 비활성화
                transaction.changeActive(IsActive.N);
                //잔액 복구
                account.adjustBalance(transaction.getAmount());

                repository.save(transaction);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    //월별 거래내역 총 합계
    public MonthlySummary getMonthlySummary(YearMonth yearMonth, String username) {

        List<Transaction> transactionList = getMonthlyTransactions(yearMonth, username);

        //월 별 총 수입
        int totalIncome = transactionList
                .stream()
                .filter(transaction -> transaction.getCategory().getStatus() == CategoryStatus.INCOME)
                .mapToInt(Transaction::getAmount)
                .sum();

        //월 별 총 지출
        int totalExpense = transactionList
                .stream()
                .filter(transaction -> transaction.getCategory().getStatus() == CategoryStatus.EXPENSE)
                .mapToInt(Transaction::getAmount)
                .sum();

        return MonthlySummary.builder()
                .yearMonth(yearMonth)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .build();
    }


    //월별 하루 거래내역 리스트
    public List<DailySummary> getDaySummary(YearMonth yearMonth, String username) {

        CategoryStatus income = CategoryStatus.INCOME;
        CategoryStatus expense = CategoryStatus.EXPENSE;

        List<Transaction> transactions = getMonthlyTransactions(yearMonth, username);

        //거래내역 당일 수입, 지출 분류
        Map<LocalDate, Integer> incomeMap = classifyTransactionByLocalDate(transactions, income);
        Map<LocalDate, Integer> expenseMap = classifyTransactionByLocalDate(transactions, expense);


        //분류된 당일 수입, 지출 합계
        List<DailySummary> dailySummaries = new ArrayList<>();
        makeDailySummary(incomeMap, income, dailySummaries);
        makeDailySummary(expenseMap, expense, dailySummaries);

        return dailySummaries;
    }

//지출 그래프
    public List<ExpenseGraph> getExpenseGraph(String startDate, String endDate, String username) {


        List<Transaction> transactions = getTransactionsByPeriod(startDate, endDate, username);

        Map<String, Integer> expenseMap = classifyTransactionByCategoryName(transactions, CategoryStatus.EXPENSE);

        return expenseMap.entrySet().stream()
                        .map(e -> ExpenseGraph.toDto(e.getKey(), e.getValue()))
                        .toList();
    }



    private static void validationAuthorization(String username, Account account, Category category, String message) {
        if (!(account.getMember().getUsername().equals(username) && category.getMember().getUsername().equals(username))) {
            throw new UnauthorizedException(message);
        }
    }

    private List<Long> getAccountIds(String username) {
        List<Long> accountIds = accountService.getAccountsByUsername(username)
                .stream()
                .map(RespAccount::getId)
                .toList();
        return accountIds;
    }


    private List<Transaction> getMonthlyTransactions(YearMonth yearMonth, String username) {

        List<Long> accountIds = getAccountIds(username);

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Transaction> transactions = repository.findByDateAndAccountIdIn(startDate, endDate, accountIds);

        return transactions;
    }

    private List<Transaction> getTransactionsByPeriod(String startDate, String endDate, String username) {

        List<Long> accountIds = getAccountIds(username);

        LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");

        List<Transaction> transactions = repository.findByDateAndAccountIdIn(startDateTime, endDateTime, accountIds);
        return transactions;
    }

    private void makeDailySummary(Map<LocalDate, Integer> map, CategoryStatus value, List<DailySummary> dailySummaries) {
        for (Map.Entry<LocalDate, Integer> e : map.entrySet()) {
            dailySummaries.add(DailySummary.builder()
                    .date(e.getKey())
                    .status(value)
                    .total(e.getValue())
                    .build());
        }
    }

    private Map<LocalDate, Integer> classifyTransactionByLocalDate(List<Transaction> transactions, CategoryStatus status) {

        Map<LocalDate, Integer> map = new HashMap<>();

        for (Transaction transaction : transactions) {
            LocalDate key = transaction.getTransacDate().toLocalDate();
            int amount = transaction.getAmount();
            CategoryStatus categoryStatus = transaction.getCategory().getStatus();

            if (status == categoryStatus &&( status ==CategoryStatus.INCOME || transaction.getActive() ==IsActive.Y)) {
                map.put(key, map.getOrDefault(key, 0) + amount);
            }

        }
        return map;
    }

    private Map<String, Integer> classifyTransactionByCategoryName(List<Transaction> transactions, CategoryStatus status) {

        Map<String, Integer> map = new HashMap<>();

        for (Transaction transaction : transactions) {
            String key = transaction.getCategory().getName();
            int amount = transaction.getAmount();
            CategoryStatus categoryStatus = transaction.getCategory().getStatus();

            if (status == categoryStatus &&( status ==CategoryStatus.INCOME || transaction.getActive() ==IsActive.Y)) {
                map.put(key, map.getOrDefault(key, 0) + amount);
            }

        }
        return map;
    }

    private static void InsufficientBalance(ReqTransaction reqTransaction, Category category, Account account) {
        boolean isIncome = category.getStatus() == CategoryStatus.INCOME;
        int transactionAmount = reqTransaction.getAmount();

        if (isIncome) {
            account.adjustBalance(transactionAmount);
        } else {
            if (account.getBalance() < transactionAmount) {
                throw new InsufficientBalanceException("잔액이 부족합니다.");
            }
            account.adjustBalance(-transactionAmount);
        }
    }

    public List<Transaction> getTransactionsByCategoryIdAndDate(Long categoryId, LocalDate startDate, LocalDate endDate) {

        return repository.findByDateANDCategoryId(categoryId, startDate.atStartOfDay(),
                endDate.atTime(23,59,59));
    }
}
