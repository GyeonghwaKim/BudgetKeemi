package com.example.budgeKeemi.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.YearMonth;

@Getter
public class MonthlySummary {

    private YearMonth yearMonth;
    private int totalIncome;
    private int totalExpense;

    @Builder
    public MonthlySummary(YearMonth yearMonth, int totalIncome, int totalExpense) {
        this.yearMonth = yearMonth;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }
}
