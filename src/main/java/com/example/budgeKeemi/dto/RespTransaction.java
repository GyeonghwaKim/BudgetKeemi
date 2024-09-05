package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RespTransaction {

    private Long id;
    private Long accountId;
    private String categoryName;
    private int amount;
    private LocalDateTime transacDate;
    private String description;

    @Builder
    public RespTransaction(Long id, Long accountId, String categoryName, int amount, LocalDateTime transacDate, String description) {
        this.id = id;
        this.accountId = accountId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.transacDate = transacDate;
        this.description = description;
    }



    public static RespTransaction toDto(Transaction transaction){
        return RespTransaction.builder()
                .id(transaction.getId())
                .categoryName(transaction.getCategory().getName())
                .amount(transaction.getAmount())
                .transacDate(transaction.getTransacDate())
                .description(transaction.getDescription())
                .build();
    }

}
