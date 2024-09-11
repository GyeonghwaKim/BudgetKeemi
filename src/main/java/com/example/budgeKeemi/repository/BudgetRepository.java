package com.example.budgeKeemi.repository;

import com.example.budgeKeemi.domain.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long> {
    List<Budget> findAllByCategoryIdIn(List<Long> categoryIds);

    Optional<Budget> findByCategoryId(Long categoryId);
}
