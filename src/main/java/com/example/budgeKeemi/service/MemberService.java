package com.example.budgeKeemi.service;

import com.example.budgeKeemi.dto.JoinForm;
import com.example.budgeKeemi.domain.Member;
import com.example.budgeKeemi.domain.RespMember;
import com.example.budgeKeemi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository repository;

    public RespMember joinMember(JoinForm joinForm) {
        Member member=JoinForm.toEntity(joinForm);

        Member savedMember=repository.save(member);

        RespMember respMember=RespMember.toDto(savedMember);

        return respMember;
    }

    public RespMember getMemberProfile(Long id) {
        Optional<Member> _member = repository.findById(id);

        if(_member.isPresent()){
            return RespMember.toDto(_member.get());
        }else {
            return null;
        }
    }
}
