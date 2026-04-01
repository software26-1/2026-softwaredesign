package com.school.management.global.config;

import com.school.management.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//스프링 security 전체 설정 파일 , 어떤 url 허용,차단,세션 쓸지말지,어떤필터 끼울지 등
//config -> 설정 앱이 시작될 때 딱 한 번 세팅되는 것들 모아두는 곳

@Configuration //bean 설정 파일임을 표시
@EnableWebSecurity //Spring Security 활성화
@EnableMethodSecurity //나중에 컨트롤러에서 @PreAuthorize("hasRole('TEACHER')") 같은 애노테이션 쓸 수 있게 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private  final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) //jwt + stateless -> csrf 공격 불가이르모 비활성화
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  //stateless 설정 > 세션(서버 단에서 id 저장 등) x
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/api-docs/**").permitAll() //로그인,토큰재발급 등은 토큰 없어도 접근 가능
                        .anyRequest().authenticated())  // 그 외 모든 url은 토큰이 있어야 접근 가능
                // 요청이 컨트롤러 도달하기 전에 여러 개의 필터를 거치는데 UsernamePasswordAuthenticationFilter (기본필터) 전에 우리가 만든 jwt커스텀 필터 끼워넣기
                // 기본 폼은 id/pw -> 서버 -> 세션 발급인데 우리는 jwt 쓰므로 jwt 꺼내서 검증기능을 따로 만들어야 함(기본 필터에 jwt 검증이 없어서)
                // 이 필터에서는 우리 서버가 만든 필터가 맞는지 검증하는 필터고 role 체크는 컨트롤러 단에서 함
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //비밀번호 db에 그대로 저장 x , BCrypt로 해시해서 저장  -> 단방향 해시라 복호화 불가
        // 사용자가 비번 입력하면 해시값과 비교 -> 우리는 비번 그대로 저장 안함 (여러 서비스들이 원래 비번 안알려주고 새로 설정하라고 하는 이유)
    }
}
