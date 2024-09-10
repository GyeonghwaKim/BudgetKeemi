package com.example.budgeKeemi.domain.entity;


import com.example.budgeKeemi.domain.type.CategoryStatus;
import com.example.budgeKeemi.domain.type.IsActive;
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

    @Enumerated(EnumType.STRING)
    private IsActive active=IsActive.Y;

    @Builder
    public Category(Long id, String name, CategoryStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public void replaceName(String name) {
        this.name=name;
    }

    public void replaceName(CategoryStatus status) {
        this.status=status;
    }

    public void updateMember(Member member) {
        this.member=member;
    }

    public void changeActive(IsActive value) {
        this.active=value;
    }
}
