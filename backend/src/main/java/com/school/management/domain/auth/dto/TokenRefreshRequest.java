package com.school.management.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// accessToken 만료 시 재발급 요청에 쓰는 DTO
// 프론트엔드가 저장해둔 refreshToken을 서버에 보내면, 서버가 새 accessToken + refreshToken 발급
@Getter
@Setter
@NoArgsConstructor
public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;
}