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
    private String profileImge;

    @Builder
    public RespMember(Long id, String username, String email, LocalDateTime joinDate,String profileImge) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.joinDate = joinDate;
        this.profileImge = profileImge;
    }

    public static RespMember toDto(Member savedMember) {
        return RespMember.builder()
                .id(savedMember.getId())
                .username(savedMember.getUsername())
                .email(savedMember.getEmail())
                .joinDate(savedMember.getJoinDate())
                .profileImge(savedMember.getProfileImg().getStoredFileName())
                .build();
    }
}
