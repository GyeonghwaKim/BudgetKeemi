package com.example.budgeKeemi.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RespMember {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime joinDate;

    @Builder
    public RespMember(Long id, String name, String email, LocalDateTime joinDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.joinDate = joinDate;
    }

    public static RespMember toDto(Member savedMember) {
        return RespMember.builder()
                .id(savedMember.getId())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .joinDate(savedMember.getJoinDate())
                .build();
    }
}
