package com.example.budgeKeemi.dto;

import com.example.budgeKeemi.domain.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReqTransaction {

    private Long accountId;
    private Long categoryId;
    private int amount;
    private LocalDateTime transacDate;
    private String description;


    public static Transaction toEntity(ReqTransaction reqTransaction) {

        return Transaction.builder()
                .amount(reqTransaction.getAmount())
                .transacDate(reqTransaction.getTransacDate())
                .description(reqTransaction.getDescription())
                .build();

    }

}
