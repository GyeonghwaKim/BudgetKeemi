package com.example.budgeKeemi.dto.resp;

import com.example.budgeKeemi.domain.type.CategoryStatus;
import com.example.budgeKeemi.domain.entity.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RespTransaction {

    private Long id;
    private Long accountId;
    private String categoryName;
    private CategoryStatus categoryStatus;
    private int amount;
    private LocalDateTime transacDate;
    private String description;

    @Builder
    public RespTransaction(Long id, Long accountId, String categoryName, CategoryStatus categoryStatus, int amount, LocalDateTime transacDate, String description) {
        this.id = id;
        this.accountId = accountId;
        this.categoryName = categoryName;
        this.categoryStatus = categoryStatus;
        this.amount = amount;
        this.transacDate = transacDate;
        this.description = description;
    }

    public static RespTransaction toDto(Transaction transaction){
        return RespTransaction.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .categoryName(transaction.getCategory().getName())
                .categoryStatus(transaction.getCategory().getStatus())
                .amount(transaction.getAmount())
                .transacDate(transaction.getTransacDate())
                .description(transaction.getDescription())
                .build();
    }

}
