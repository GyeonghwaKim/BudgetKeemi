package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.entity.ProfileImg;
import com.example.budgeKeemi.domain.entity.Member;
import com.example.budgeKeemi.dto.resp.RespMember;
import com.example.budgeKeemi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository repository;

    private final ProfileImgService profileImgService;

    public RespMember getMemberProfile(String username) {
        Optional<Member> _member = repository.findByUsername(username);

        if(_member.isPresent()){
            return RespMember.toDto(_member.get());
        }else {
            return null;
        }
    }

    public Member getMemberByUsername(String username) {
        Optional<Member> _member = repository.findByUsername(username);

        if(_member.isPresent()){
            return _member.get();
        }else{
            return null;
        }
    }

    public RespMember updateProfileImg(MultipartFile multipartFile, String username) {

        Optional<Member> _member = repository.findByUsername(username);
        if(_member.isPresent()){
            Member member = _member.get();

            ProfileImg profileImg = profileImgService.saveProfileImg(multipartFile);
            member.updateProfileImge(profileImg);
            Member saveMember = repository.save(member);

            return RespMember.toDto(saveMember);
        }else {
            return null;
        }
    }
}
