package com.example.budgeKeemi.dto.req;

import com.example.budgeKeemi.domain.entity.Account;
import com.example.budgeKeemi.domain.type.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReqAccount {

    @NotBlank(message = "계좌 이름을 작성하세요")
    private String name;

    @NotNull(message = "계좌 유형을 선택하세요")
    private AccountType status;

    @Positive(message = "유효한 금액을 입력하세요")
    private int balance;

    public static Account toEntity(ReqAccount req){
        return Account.builder()
                .name(req.getName())
                .status(req.getStatus())
                .balance(req.getBalance())
                .createDate(LocalDateTime.now())
                .build();
    }
}
