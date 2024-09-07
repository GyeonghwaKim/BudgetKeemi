package com.example.budgeKeemi.repository;

import com.example.budgeKeemi.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query("SELECT t FROM Transaction t WHERE t.transacDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByDate(LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByCategoryId(Long categoryId);
}
