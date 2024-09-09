package com.example.budgeKeemi.dto.resp;

import com.example.budgeKeemi.domain.entity.Budget;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RespBudget {
    private Long id;

    private int goalAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private String categoryName;

    private Long categoryId;

    private int useAmount;

    @Builder
    public RespBudget(Long id, int goalAmount, LocalDate startDate, LocalDate endDate, String categoryName, Long categoryId) {
        this.id = id;
        this.goalAmount = goalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }



    public static RespBudget toDto(Budget budget){
        return RespBudget.builder()
                .id(budget.getId())
                .goalAmount(budget.getGoalAmount())
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .categoryName(budget.getCategory().getName())
                .categoryId(budget.getCategory().getId())
                .build();
    }

    public void updateUseAmount(Integer integer) {
        this.useAmount=integer;
    }
}
