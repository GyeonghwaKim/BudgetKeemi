package com.example.budgeKeemi.config;

import com.example.budgeKeemi.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class securityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrf)->csrf.disable())

                .oauth2Login(Customizer.withDefaults());
//
//                .formLogin((login) ->login.disable())
//
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/","/oauth2/**","/login/**").permitAll()
//                        .anyRequest().authenticated());
//
//        http
//                .oauth2Login((oauth2)-> oauth2
//                .loginPage("/login")
//                .failureUrl("/login?error=true")
//                .userInfoEndpoint((userInfoEndpointConfig ->
//                        userInfoEndpointConfig.userService(customOAuth2UserService))));
//

        return http.build();
    }




}
