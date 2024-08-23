package com.example.budgeKeemi.domain;

public enum CategoryStatus {
    INCOME("수입"),EXPENSE("지출");

    private final String description;

    CategoryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
