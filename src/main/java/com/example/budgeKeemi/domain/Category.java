package com.example.budgeKeemi.domain;


import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryStatus status;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

}
