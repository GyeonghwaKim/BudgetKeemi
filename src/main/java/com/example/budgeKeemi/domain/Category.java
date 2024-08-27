package com.example.budgeKeemi.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryStatus status;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Builder
    public Category(Long id, String name, CategoryStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public void updateName(String name) {
        this.name=name;
    }

    public void updateStatus(CategoryStatus status) {
        this.status=status;
    }
}
