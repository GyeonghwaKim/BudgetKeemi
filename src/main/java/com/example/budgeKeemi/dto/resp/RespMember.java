package com.example.budgeKeemi.dto.resp;

import com.example.budgeKeemi.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RespMember {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime joinDate;

    @Builder
    public RespMember(Long id, String username, String email, LocalDateTime joinDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.joinDate = joinDate;
    }

    public static RespMember toDto(Member savedMember) {
        return RespMember.builder()
                .id(savedMember.getId())
                .username(savedMember.getUsername())
                .email(savedMember.getEmail())
                .joinDate(savedMember.getJoinDate())
                .build();
    }
}
