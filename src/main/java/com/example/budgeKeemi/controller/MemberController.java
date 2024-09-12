package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.resp.RespMember;
import com.example.budgeKeemi.dto.req.JoinForm;
import com.example.budgeKeemi.oauth.CustomOAuth2User;
import com.example.budgeKeemi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

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
    @GetMapping("/profile")
    public ResponseEntity<?> getMemberProfile(Principal principal) {
        String username = getUsername(principal);

        RespMember respMember=service.getMemberProfile(username);

        if(respMember==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(respMember);
    }

    @PutMapping("/profileImg")
    public ResponseEntity<?> updateProfileImg(@RequestParam("profileImg")MultipartFile multipartFile, Principal principal){
        String username = getUsername(principal);

        RespMember respMember = service.updateProfileImg(multipartFile, username);

        if(respMember==null){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(respMember,HttpStatus.CREATED);
    }


    private static String getUsername(Principal principal){
        String username="";

        if(principal instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            OAuth2User oAuth2User = authToken.getPrincipal();

            if(oAuth2User instanceof CustomOAuth2User) {
                CustomOAuth2User customOAuth2User = (CustomOAuth2User) oAuth2User;
                username=customOAuth2User.getUsername();
            }
        }

        return username;
    }


}
