package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Account;
import com.example.budgeKeemi.domain.AccountType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RespAccount {

    private Long id;
    private String name;
    private AccountType status;
    private int balance;
    private LocalDateTime createDate;


    @Builder
    public RespAccount(Long id, String name, AccountType status, int balance, LocalDateTime createDate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.balance = balance;
        this.createDate = createDate;
    }

    public static RespAccount toDto(Account account) {
        return RespAccount.builder()
                .id(account.getId())
                .name(account.getName())
                .status(account.getStatus())
                .balance(account.getBalance())
                .createDate(account.getCreateDate())
                .build();
    }
}
