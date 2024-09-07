package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Budget;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReqBudget {

    private int goalAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long categoryId;

    public static Budget toEntity(ReqBudget reqBudget) {
        return Budget.builder()
                .goalAmount(reqBudget.getGoalAmount())
                .startDate(reqBudget.getStartDate())
                .endDate(reqBudget.getEndDate())
                .build();
    }
}
