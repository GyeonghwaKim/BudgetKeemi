package com.example.budgeKeemi.dto.req;

import com.example.budgeKeemi.domain.entity.Category;
import com.example.budgeKeemi.domain.type.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReqCategory {

    @NotBlank(message = "카테고리 이름을 작성하세요")
    private String name;

    @NotNull(message = "카테고리 유형을 선택하세요")
    private CategoryStatus status;


    public static Category toEntity(ReqCategory reqCategory) {
        return Category.builder()
                .name(reqCategory.getName())
                .status(reqCategory.getStatus())
                .build();
    }
}
