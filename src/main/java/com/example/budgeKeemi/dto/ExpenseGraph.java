package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Transaction;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExpenseGraph {

    private String categoryName;
    private double amount;

    @Builder
    public ExpenseGraph(String categoryName, double amount) {
        this.categoryName = categoryName;
        this.amount = amount;
    }


    public static ExpenseGraph toDto(String categoryName,int amount) {
        return ExpenseGraph.builder()
                .categoryName(categoryName)
                .amount(amount)
                .build();
    }
}
