package com.school.management.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 비밀번호 초기화 DTO
// ADMIN 또는 TEACHER가 학생 비밀번호를 임시 비밀번호로 리셋할 때 사용
// (학생이 본인 비밀번호 변경하는 것과 다름 - 그건 currentPassword도 같이 받아야 함)
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetRequest {

    @NotBlank
    private String username;     // 비밀번호를 초기화할 대상 학생의 username

    @NotBlank
    private String newPassword;  // 새 임시 비밀번호 (이후 학생이 직접 변경해야 함)
}