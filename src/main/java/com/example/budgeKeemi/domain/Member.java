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
    private String username;
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Builder
    public Member(String username, String email, MemberRole role, LocalDateTime joinDate) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.joinDate = joinDate;
    }
}
