package com.school.management.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 로그인 요청 시 프론트엔드 → 서버로 전달되는 데이터
@Getter
@Setter // Jackson이 JSON → Java 변환 시 private 필드에 값을 넣으려면 setter 필요
@NoArgsConstructor
public class LoginRequest {

    @NotBlank // null, 빈 문자열(""), 공백만 있는 문자열 모두 거부
    private String username;

    @NotBlank
    private String password;
}
