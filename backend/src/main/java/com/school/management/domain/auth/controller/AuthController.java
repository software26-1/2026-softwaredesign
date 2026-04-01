package com.school.management.domain.auth.controller;

import com.school.management.domain.auth.dto.*;
import com.school.management.domain.auth.service.AuthService;
import com.school.management.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController // @Controller + @ResponseBody. JSON 응답을 자동으로 처리
@RequestMapping("/api/v1/auth") // 이 컨트롤러의 모든 메서드는 /api/v1/auth로 시작
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API (로그인/로그아웃/토큰 재발급/비밀번호 초기화)")
public class AuthController {

    private final AuthService authService;

    // ────────────────────────────────────────────────
    // POST /api/v1/auth/login
    // SecurityConfig에서 permitAll() 설정 → 토큰 없어도 접근 가능
    // ────────────────────────────────────────────────
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "username + password로 로그인. 성공 시 accessToken, refreshToken 반환")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) { // @Valid: LoginRequest의 @NotBlank 등 검증 실행
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
    }

    // ────────────────────────────────────────────────
    // POST /api/v1/auth/logout
    // 로그인된 상태여야 하므로 토큰 필요
    // @AuthenticationPrincipal: JwtAuthenticationFilter가 SecurityContext에 저장한 인증 정보를 꺼냄
    // ────────────────────────────────────────────────
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "Redis에서 refresh token 삭제. 이후 토큰 재발급 불가")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserDetails userDetails) {
        // CustomUserDetailsService에서 username으로 userId(String)를 저장했으므로 다시 Long으로 변환
        Long userId = Long.parseLong(userDetails.getUsername());
        authService.logout(userId);
        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
    }

    // ────────────────────────────────────────────────
    // POST /api/v1/auth/refresh
    // accessToken 만료 시 refreshToken으로 새 토큰 발급
    // SecurityConfig에서 permitAll() 설정 → accessToken 없어도 접근 가능
    // ────────────────────────────────────────────────
    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "만료된 accessToken을 refreshToken으로 재발급")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @Valid @RequestBody TokenRefreshRequest request) {
        LoginResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponse.success("토큰 재발급 성공", response));
    }

    // ────────────────────────────────────────────────
    // POST /api/v1/auth/password/reset
    // ADMIN 또는 TEACHER만 접근 가능
    // @PreAuthorize: SecurityConfig의 @EnableMethodSecurity 덕분에 동작
    // ────────────────────────────────────────────────
    @PostMapping("/password/reset")
    @Operation(summary = "비밀번호 초기화", description = "ADMIN 또는 TEACHER가 학생 비밀번호를 임시 비밀번호로 초기화")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')") // STUDENT, PARENT는 이 API 호출 불가
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("비밀번호 초기화 완료"));
    }
}