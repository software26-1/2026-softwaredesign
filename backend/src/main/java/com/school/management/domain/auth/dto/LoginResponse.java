package com.school.management.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 로그인 성공 시 서버 → 프론트엔드로 돌려주는 데이터
@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;   // API 호출마다 헤더에 담아 보내는 토큰 (30분 유효)
    private String refreshToken;  // accessToken 만료 시 재발급 요청에 쓰는 토큰 (7일 유효)
    private String role;          // 프론트엔드에서 어떤 화면(교사/학생/학부모)을 보여줄지 결정하는 데 사용
}