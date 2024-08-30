package com.example.budgeKeemi.config;

import com.example.budgeKeemi.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class securityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrf)->csrf.disable())
                .oauth2Login(Customizer.withDefaults())
                .oauth2Login((oauth2)-> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint((userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/oauth2/**","/login/**").permitAll()
                        .anyRequest().authenticated());
        
        return http.build();
    }
}
