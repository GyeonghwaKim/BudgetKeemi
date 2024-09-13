package com.example.budgeKeemi.dto.req;

import com.example.budgeKeemi.domain.entity.Budget;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReqBudget {


    @Positive(message = "유효한 금액을 입력하세요")
    private int goalAmount;

    @NotNull(message = "시작 날짜를 입력하세요")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜를 입력하세요")
    private LocalDate endDate;

    //DuplicateCategoryException
    private Long categoryId;

    public static Budget toEntity(ReqBudget reqBudget) {
        return Budget.builder()
                .goalAmount(reqBudget.getGoalAmount())
                .startDate(reqBudget.getStartDate())
                .endDate(reqBudget.getEndDate())
                .build();
    }
}
