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

    private Long categoryId;

    @Builder
    public RespBudget(Long id, int amount, LocalDate startDate, LocalDate endDate, Long categoryId) {
        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = categoryId;
    }

    public static RespBudget toDto(Budget budget){
        return RespBudget.builder()
                .id(budget.getId())
                .amount(budget.getAmount())
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .categoryId(budget.getCategory().getId())
                .build();
    }
}
