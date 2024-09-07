package com.example.budgeKeemi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goal_amount")
    private int goalAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Budget(Long id, int goalAmount, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.goalAmount = goalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addCategory(Category category) {
        this.category = category;
    }

    public void updateAmount(int amount) {
        this.goalAmount = amount;
    }

    public void updateEndDate(LocalDate endDate) {
        this.endDate=endDate;
    }
}
