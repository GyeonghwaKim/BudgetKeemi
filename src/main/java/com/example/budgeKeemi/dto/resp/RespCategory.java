package com.example.budgeKeemi.dto.resp;

import com.example.budgeKeemi.domain.entity.Category;
import com.example.budgeKeemi.domain.type.CategoryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RespCategory {

    private Long id;

    private String name;
    private CategoryStatus status;

    @Builder
    public RespCategory(Long id, String name, CategoryStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static RespCategory toDto(Category category) {
        return RespCategory.builder()
                .id(category.getId())
                .name(category.getName())
                .status(category.getStatus())
                .build();

    }
}
