package com.example.budgeKeemi.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Budget {

    @Id @GeneratedValue
    private Long id;

    private int amount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
