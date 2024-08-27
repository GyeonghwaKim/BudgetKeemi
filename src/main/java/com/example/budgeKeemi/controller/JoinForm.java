package com.example.budgeKeemi.controller;


import com.example.budgeKeemi.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JoinForm {
    private String name;
    private String email;
    private String password;


    public static Member toEntity(JoinForm joinForm) {
        return Member.builder()
                .name(joinForm.getName())
                .email(joinForm.getEmail())
                .password(joinForm.getPassword())
                .joinDate(LocalDateTime.now())
                .build();

    }
}
