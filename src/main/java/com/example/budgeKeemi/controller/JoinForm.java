package com.example.budgeKeemi.controller;


import com.example.budgeKeemi.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JoinForm {
    private String username;
    private String email;


    public static Member toEntity(JoinForm joinForm) {
        return Member.builder()
                .username(joinForm.getUsername())
                .email(joinForm.getEmail())
                .joinDate(LocalDateTime.now())
                .build();

    }
}
