package com.example.budgeKeemi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String name;
    private String email;
    private String password;

    @Column(name = "join_date")
    private LocalDateTime joinDate;
}
