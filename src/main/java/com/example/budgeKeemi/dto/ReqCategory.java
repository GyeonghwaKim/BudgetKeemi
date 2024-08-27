package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Category;
import com.example.budgeKeemi.domain.CategoryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReqCategory {
    private String name;
    private CategoryStatus status;


    public static Category toEntity(ReqCategory reqCategory) {
        return Category.builder()
                .name(reqCategory.getName())
                .status(reqCategory.getStatus())
                .build();
    }
}
