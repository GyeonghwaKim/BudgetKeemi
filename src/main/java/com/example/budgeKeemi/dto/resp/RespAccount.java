package com.example.budgeKeemi.dto.resp;

import com.example.budgeKeemi.domain.entity.Account;
import com.example.budgeKeemi.domain.type.IsActive;
import com.example.budgeKeemi.domain.type.AccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class RespAccount {

    private Long id;
    private String name;
    private AccountType status;
    private long balance;
    private LocalDateTime createDate;
    private IsActive active;


    @Builder
    public RespAccount(Long id, String name, AccountType status, long balance, LocalDateTime createDate, IsActive active) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.balance = balance;
        this.createDate = createDate;
        this.active=active;
    }

    public static RespAccount toDto(Account account) {
        return RespAccount.builder()
                .id(account.getId())
                .name(account.getName())
                .status(account.getStatus())
                .balance(account.getBalance())
                .createDate(account.getCreateDate())
                .active(account.getActive())
                .build();
    }
}
