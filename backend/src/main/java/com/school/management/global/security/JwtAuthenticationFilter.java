package com.school.management.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //요청 당 한번 실행되는 필터 클래스 상속 , dofilterinternal 구현

    private  final JwtProvider jwtProvider;
    private  final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException { //필터 특성 상 선언해야하는 예외들
        String token = resolveToken(request); //헤더에서 토큰 꺼내는 메서드 호출,없으면 null 반환

        if(token != null && jwtProvider.validateToken(token)) { //토큰이 있고 유효할때만 인증 처리
            Long userId = jwtProvider.getUserId(token); //토큰 안에서 userId 꺼냄
            UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(userId)); //db에서 조회해서 userdetails 로 반환(커스텀유저디테일에서 만든거)
            // 스프링 세큐리티 인증 객체 , userdetailsdhk 권한 목록 담음
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //SecurityContext에 인증 정보 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
    // Authorization 헤더에서 Bearer  제거하고 순수 토큰만 반환
    // Bearer  가 7글자라 substring(7)
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}

/* spring security에 등록하는 이유
등록 안 하면 Spring Security가 "이 사람 누구야? 인증 안 됐어" 로 처리
등록 시 가능한 것들

1.컨트롤러에서 현재 로그인한 유저 바로 꺼내기
@GetMapping("/me")
public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails) {
    String userId = userDetails.getUsername();  // 로그인한 유저 ID
}

2.role 체크
@PreAuthorize("hasRole('TEACHER')")  // TEACHER만 접근 가능

  ---
"이 요청이 누구 거야?"를 Spring Security가 알 수 있게 등록. 등록 안 하면 매번 토큰 직접 파싱해야 하고 role 체크도 직접 구현해야 함. */