package com.example.budgeKeemi.dto.req;

import com.example.budgeKeemi.domain.entity.Category;
import com.example.budgeKeemi.domain.type.CategoryStatus;
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
