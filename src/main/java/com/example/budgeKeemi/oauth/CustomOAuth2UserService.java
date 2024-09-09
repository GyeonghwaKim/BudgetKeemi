package com.example.budgeKeemi.oauth;

import com.example.budgeKeemi.domain.entity.Member;
import com.example.budgeKeemi.domain.type.MemberRole;
import com.example.budgeKeemi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User.getAttributes() = "+oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response=null;

        if(registrationId.equals("naver")){
            oAuth2Response=new NaverResponse(oAuth2User.getAttributes());
        }else if(registrationId.equals("google")){
            oAuth2Response=new GoogleResponse(oAuth2User.getAttributes());
        }else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        log.info("oauth username = {}",username);
        Optional<Member> _member = memberRepository.findByUsername(username);
        MemberRole role = MemberRole.USER;
        //String role = "ROLE_USER";

        if(_member.isPresent()){
            Member member = _member.get();
            member.updateUsername(username);
            member.updateEmail(oAuth2Response.getEmail());

            role=member.getRole();

            memberRepository.save(member);

        }else{

            Member member = Member.builder()
                    .role(role)
                    .username(username)
                    .email(oAuth2Response.getEmail())
                    .joinDate(LocalDateTime.now())
                    .build();

            memberRepository.save(member);

        }
        return new CustomOAuth2User(oAuth2Response,role);
        }

    }
