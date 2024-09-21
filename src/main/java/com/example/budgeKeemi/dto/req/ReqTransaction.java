package com.example.budgeKeemi.dto.req;

import com.example.budgeKeemi.domain.entity.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReqTransaction {

    @NotNull(message = "계좌를 선택하세요")
    private Long accountId;

    @NotNull(message = "카테고리를 선택하세요")
    private Long categoryId;

    @Positive(message = "유효한 금액을 입력하세요")
    private long amount;

    @NotNull(message = "거래 날짜를 선택하세요")
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
