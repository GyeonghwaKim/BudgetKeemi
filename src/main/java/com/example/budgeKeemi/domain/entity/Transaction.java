package com.example.budgeKeemi.domain.entity;

import com.example.budgeKeemi.domain.type.IsActive;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    @Column(name = "transac_date")
    private LocalDateTime transacDate;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name ="account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IsActive active= IsActive.Y;


    @Builder
    public Transaction(int amount, LocalDateTime transacDate, String description) {
        this.amount = amount;
        this.transacDate = transacDate;
        this.description = description;
    }


    public void addAccount(Account account) {
        this.account=account;
    }

    public void addCategory(Category category) {
        this.category=category;
    }

    public void updateAmount(int amount) {
        this.amount=amount;
    }

    public void updateDescription(String description) {
        this.description=description;
    }

    public void changeActive(IsActive value) {
        this.active=value;
    }
}
