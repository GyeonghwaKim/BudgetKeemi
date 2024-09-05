package com.example.budgeKeemi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType status;

    private int balance;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public Account(String name, AccountType status, int balance, LocalDateTime createDate) {
        this.name = name;
        this.status = status;
        this.balance = balance;
        this.createDate = createDate;
    }


    public void updateBalence(int amount) {
        this.balance=this.balance+amount;
    }
}
