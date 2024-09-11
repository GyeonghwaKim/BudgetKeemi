package com.example.budgeKeemi.repository;

import com.example.budgeKeemi.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query("SELECT t FROM Transaction t WHERE t.transacDate BETWEEN :startDate AND :endDate AND t.account.id IN :accountIds")
    List<Transaction> findByDateAndAccountIdIn(LocalDateTime startDate, LocalDateTime endDate, List<Long> accountIds);

    List<Transaction> findAllByAccountIdInOrderByTransacDateDesc(List<Long> accountIds);
}
