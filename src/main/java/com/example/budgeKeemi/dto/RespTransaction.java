package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RespTransaction {

    private Long id;
    private Long accountId;
    private Long categoryId;
    private int amount;
    private LocalDateTime transacDate;
    private String description;

    @Builder
    public RespTransaction(Long id, Long accountId, Long categoryId, int amount, LocalDateTime transacDate, String description) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.transacDate = transacDate;
        this.description = description;
    }

    public static RespTransaction toDto(Transaction transaction){
        return RespTransaction.builder()
                .id(transaction.getId())
        //        .accountId(transaction.getAccount().getId())
        //        .categoryId(transaction.getCategory().getId())
                .amount(transaction.getAmount())
                .transacDate(transaction.getTransacDate())
                .description(transaction.getDescription())
                .build();
    }

}
