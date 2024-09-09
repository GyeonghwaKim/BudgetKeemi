package com.example.budgeKeemi.dto.resp;

import com.example.budgeKeemi.domain.type.CategoryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RespCategoryStatus {

    private String category;
    private String description;

    @Builder
    public RespCategoryStatus(String category, String description) {
        this.category = category;
        this.description = description;
    }

    public static RespCategoryStatus toDto(CategoryStatus categoryStatus) {
        return RespCategoryStatus.builder()
                .category(categoryStatus.name())
                .description(categoryStatus.getDescription())
                .build();
    }
}
