package com.example.budgeKeemi.domain.entity;

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
    private long goalAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Budget(Long id, long goalAmount, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.goalAmount = goalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addCategory(Category category) {
        this.category = category;
    }

    public void replaceGoalAmount(long goalAmount) {
        this.goalAmount = goalAmount;
    }

    public void replaceEndDate(LocalDate endDate) {
        this.endDate=endDate;
    }

    public void replaceCategory(Category category) {
        this.category=category;
    }

    public void replaceStartDate(LocalDate startDate) {
        this.startDate=startDate;
    }
}
