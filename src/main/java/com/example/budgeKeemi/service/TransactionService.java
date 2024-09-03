package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.Account;
import com.example.budgeKeemi.domain.Category;
import com.example.budgeKeemi.domain.CategoryStatus;
import com.example.budgeKeemi.domain.Transaction;
import com.example.budgeKeemi.dto.*;
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

    public RespTransaction createTransaction(ReqTransaction reqTransaction) {

        Transaction transaction= ReqTransaction.toEntity(reqTransaction);


        Account account=accountService.getAccountById(reqTransaction.getAccountId());
        Category category = categoryService.getCategoryById(reqTransaction.getCategoryId());

        transaction.addAccount(account);
        transaction.addCategory(category);

        Transaction saveTransaction = this.repository.save(transaction);

        RespTransaction respTransaction = RespTransaction.toDto(saveTransaction);

        return respTransaction;
    }

    public List<RespTransaction> getTransactions() {
        List<Transaction> transactions = repository.findAll();
        List<RespTransaction> respTransactions = transactions.stream().map(transaction -> RespTransaction.toDto(transaction)).toList();
        return respTransactions;
    }

    public RespTransaction getTransactionDetail(Long id) {

        Optional<Transaction> _transaction = repository.findById(id);

        if(_transaction.isPresent()){
            return RespTransaction.toDto(_transaction.get());
        }else{
            return null;
        }
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

    public boolean deleteTransaction(Long id) {
        Optional<Transaction> _transaction = repository.findById(id);

        if(_transaction.isPresent()){

            repository.delete(_transaction.get());
            return true;

        }else{
            return false;
        }
    }

    public MonthlySummary getMonthlySummary(YearMonth yearMonth) {
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23,59,59);

        List<Transaction> transactionList=repository.findByMonth(startDate,endDate);

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

    public List<DailySummary> getDaySummary(YearMonth yearMonth) {
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23,59,59);

        List<Transaction> transactionList=repository.findByMonth(startDate,endDate);

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
}
