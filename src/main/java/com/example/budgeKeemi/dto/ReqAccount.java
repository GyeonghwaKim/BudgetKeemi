package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Account;
import com.example.budgeKeemi.domain.AccountType;
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
