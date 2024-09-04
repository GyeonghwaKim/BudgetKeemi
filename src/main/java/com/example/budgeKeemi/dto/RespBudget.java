package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Budget;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RespBudget {
    private Long id;

    private int amount;

    private LocalDate startDate;

    private LocalDate endDate;

    private String categoryName;

    @Builder
    public RespBudget(Long id, int amount, LocalDate startDate, LocalDate endDate, String categoryName) {
        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryName = categoryName;
    }



    public static RespBudget toDto(Budget budget){
        return RespBudget.builder()
                .id(budget.getId())
                .amount(budget.getAmount())
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .categoryName(budget.getCategory().getName())
                .build();
    }
}
