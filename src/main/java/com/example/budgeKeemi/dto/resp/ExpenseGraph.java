package com.example.budgeKeemi.dto.resp;

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


    public static ExpenseGraph toDto(String categoryName,long amount) {
        return ExpenseGraph.builder()
                .categoryName(categoryName)
                .amount(amount)
                .build();
    }
}
