package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.domain.RespMember;
import com.example.budgeKeemi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService service;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<?> registerMember(@RequestBody JoinForm joinForm){
        RespMember respMember=service.joinMember(joinForm);

        return new ResponseEntity<>(respMember, HttpStatus.CREATED);
    }

    //프로필 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMemberProfile(@PathVariable(name = "memberId") Long id){
        RespMember respMember=service.getMemberProfile(id);

        return ResponseEntity.ok(respMember);
    }
    




}
