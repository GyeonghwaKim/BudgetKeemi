package com.example.budgeKeemi.dto.resp;


import lombok.Builder;
import lombok.Getter;

import java.time.YearMonth;

@Getter
public class MonthlySummary {

    private YearMonth yearMonth;
    private long totalIncome;
    private long totalExpense;

    @Builder
    public MonthlySummary(YearMonth yearMonth, long totalIncome, long totalExpense) {
        this.yearMonth = yearMonth;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }
}
