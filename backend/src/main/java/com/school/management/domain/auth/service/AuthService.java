package com.school.management.domain.auth.service;

import com.school.management.domain.auth.dto.*;
import com.school.management.domain.auth.entity.User;
import com.school.management.domain.auth.repository.UserRepository;
import com.school.management.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final StringRedisTemplate redisTemplate; // Redis에 문자열 저장/조회하는 Spring 기본 제공 클라이언트

    // Redis key 형태: "refresh:123" (123은 userId)
    // prefix 붙이는 이유: Redis는 key-value 저장소라 충돌 방지용
    private static final String REFRESH_TOKEN_PREFIX = "refresh:";

    // ────────────────────────────────────────────────
    // 로그인
    // ────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // username으로 유저 조회. soft delete된 유저는 로그인 불가
        User user = userRepository.findByUsername(request.getUsername())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        // BCrypt 비밀번호 검증
        // passwordEncoder.matches(입력값, DB에 저장된 해시값) → 같으면 true
        // 보안상 "아이디가 없음" / "비밀번호 틀림"을 구분하지 않고 같은 메시지 반환
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // JWT 발급
        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        // Redis에 refresh token 저장 (7일 후 자동 만료)
        // 이후 재발급 요청 시 Redis의 값과 대조해서 탈취 여부 검증
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + user.getId(),
                refreshToken,
                7, TimeUnit.DAYS
        );

        return new LoginResponse(accessToken, refreshToken, user.getRole().name());
    }

    // ────────────────────────────────────────────────
    // 로그아웃
    // ────────────────────────────────────────────────
    public void logout(Long userId) {
        // Redis에서 refresh token 삭제
        // 이후 해당 유저의 refresh token으로 재발급 요청이 와도 Redis에 없으니 거부됨
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
    }

    // ────────────────────────────────────────────────
    // 토큰 재발급
    // ────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public LoginResponse refresh(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        // 1. refresh token 자체가 유효한지 검증 (서명, 만료 확인)
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 refresh token입니다.");
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        // 2. Redis에 저장된 refresh token과 대조
        // 로그아웃 후 탈취된 토큰으로 재발급 요청하는 것을 막음
        String stored = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
        if (!refreshToken.equals(stored)) {
            throw new IllegalArgumentException("refresh token이 일치하지 않습니다.");
        }

        // 3. 유저 정보 조회 (role 가져오기 위해)
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 4. 새 토큰 발급 + Redis 갱신 (refresh token rotation: 재발급마다 refresh token도 교체)
        String newAccessToken = jwtProvider.generateAccessToken(userId, user.getRole().name());
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + userId,
                newRefreshToken,
                7, TimeUnit.DAYS
        );

        return new LoginResponse(newAccessToken, newRefreshToken, user.getRole().name());
    }

    // ────────────────────────────────────────────────
    // 비밀번호 초기화 (ADMIN / TEACHER 전용)
    // ────────────────────────────────────────────────
    @Transactional // DB를 변경하므로 @Transactional 필요 (없으면 변경사항이 DB에 저장 안됨)
    public void resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 새 비밀번호를 BCrypt로 해시해서 저장
        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        // @Transactional이 있으면 메서드 끝날 때 JPA가 변경 감지해서 자동으로 UPDATE 쿼리 실행
    }
}