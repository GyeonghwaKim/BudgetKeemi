package com.example.budgeKeemi.dto.req;

import com.example.budgeKeemi.domain.entity.Account;
import com.example.budgeKeemi.domain.type.AccountType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReqAccount {

    private String name;
    private AccountType status;
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
