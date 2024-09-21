package com.example.budgeKeemi.dto.resp;

import com.example.budgeKeemi.domain.type.CategoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailySummary {
    private LocalDate date;
    private CategoryStatus status;
    private long total;

    @Builder
    public DailySummary(LocalDate date, CategoryStatus status, long total) {
        this.date = date;
        this.status = status;
        this.total = total;
    }

}
