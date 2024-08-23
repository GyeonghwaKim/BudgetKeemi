package com.example.budgeKeemi.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    private int amount;

    @Column(name = "transac_date")
    private LocalDateTime transacDate;

    private String description;

    @ManyToOne
    @JoinColumn(name ="account_id")
    private Account account;

}
