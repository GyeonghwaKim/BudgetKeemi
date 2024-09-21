package com.example.budgeKeemi.dto.resp;

import com.example.budgeKeemi.domain.type.CategoryStatus;
import com.example.budgeKeemi.domain.entity.Transaction;
import com.example.budgeKeemi.domain.type.IsActive;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RespTransaction {

    private Long id;
    private Long accountId;
    private String categoryName;
    private CategoryStatus categoryStatus;
    private long amount;
    private LocalDateTime transacDate;
    private String description;
    private IsActive active;

    @Builder
    public RespTransaction(Long id, Long accountId, String categoryName, CategoryStatus categoryStatus, long amount, LocalDateTime transacDate, String description,IsActive active) {
        this.id = id;
        this.accountId = accountId;
        this.categoryName = categoryName;
        this.categoryStatus = categoryStatus;
        this.amount = amount;
        this.transacDate = transacDate;
        this.description = description;
        this.active=active;
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
                .active(transaction.getActive())
                .build();
    }

}
