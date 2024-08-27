package com.example.budgeKeemi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String name;
    private String email;
    private String password;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Builder
    public Member(String name, String email, String password, LocalDateTime joinDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.joinDate = joinDate;
    }
}
