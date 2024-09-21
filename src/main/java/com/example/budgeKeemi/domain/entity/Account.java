package com.example.budgeKeemi.domain.entity;

import com.example.budgeKeemi.domain.type.AccountType;
import com.example.budgeKeemi.domain.type.IsActive;
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

    private long balance;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IsActive active= IsActive.Y;


    @Builder
    public Account(String name, AccountType status, long balance, LocalDateTime createDate) {
        this.name = name;
        this.status = status;
        this.balance = balance;
        this.createDate = createDate;
    }

    public void adjustBalance(long amount) {
        this.balance=this.balance+amount;
    }

    public void replaceName(String name) {
        this.name=name;
    }

    public void replaceBalance(long balance) {
        this.balance=balance;
    }


    public void replaceStatus(AccountType status) {
        this.status=status;
    }

    public void updateMember(Member member) {
        this.member=member;
    }

    public void changeActive(IsActive value) {
        this.active=value;
    }
}
