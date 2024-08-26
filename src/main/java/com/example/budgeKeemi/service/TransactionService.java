package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.Account;
import com.example.budgeKeemi.domain.Category;
import com.example.budgeKeemi.domain.Transaction;
import com.example.budgeKeemi.dto.ReqTransaction;
import com.example.budgeKeemi.dto.RespTransaction;
import com.example.budgeKeemi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
