package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.CategoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailySummary {
    private LocalDate date;
    private CategoryStatus status;
    private int total;

    @Builder
    public DailySummary(LocalDate date, CategoryStatus status, int total) {
        this.date = date;
        this.status = status;
        this.total = total;
    }

}
