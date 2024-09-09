package com.example.budgeKeemi.domain.entity;

import com.example.budgeKeemi.domain.type.AccountType;
import com.example.budgeKeemi.domain.type.AccountActive;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountActive active=AccountActive.Y;


    @Builder
    public Account(String name, AccountType status, int balance, LocalDateTime createDate) {
        this.name = name;
        this.status = status;
        this.balance = balance;
        this.createDate = createDate;
    }

    public void adjustBalance(int amount) {
        this.balance=this.balance+amount;
    }

    public void replaceName(String name) {
        this.name=name;
    }

    public void replaceBalance(int balance) {
        this.balance=balance;
    }


    public void replaceStatus(AccountType status) {
        this.status=status;
    }

    public void updateMember(Member member) {
        this.member=member;
    }

    public void changeActive(AccountActive value) {
        this.active=value;
    }
}
