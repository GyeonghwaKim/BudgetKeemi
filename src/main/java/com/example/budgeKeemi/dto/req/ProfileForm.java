package com.example.budgeKeemi.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
//안녕
@Getter
public class ProfileForm {

    //@Size
    @NotBlank(message = "유효한 파일을 입력하세요")
    private String storeFileName;
}
