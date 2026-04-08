package com.school.management.domain.attendance.controller;

import com.school.management.domain.attendance.dto.*;
import com.school.management.domain.attendance.service.AttendanceService;
import com.school.management.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "출결 관련 API")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 출결 등록
    @PostMapping("/api/v1/attendance")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "출결 등록", description = "학생 출결 기록 등록 [TEACHER, ADMIN]")
    public ResponseEntity<ApiResponse<Void>> createAttendance(
            @Valid @RequestBody AttendanceCreateRequest request) {
        attendanceService.createAttendance(request);
        return ResponseEntity.ok(ApiResponse.success("출결 등록 성공"));
    }

    // 출결 내역 조회
    @GetMapping("/api/v1/students/{studentId}/attendance")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT', 'PARENT')")
    @Operation(summary = "출결 내역 조회", description = "학생별 전체 출결 기록 조회")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendances(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("출결 조회 성공",
                attendanceService.getAttendances(studentId)));
    }

    // 출결 요약 조회
    @GetMapping("/api/v1/students/{studentId}/attendance/summary")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT', 'PARENT')")
    @Operation(summary = "출결 요약 조회", description = "학생별 출석/결석 일수 요약")
    public ResponseEntity<ApiResponse<AttendanceSummaryResponse>> getAttendanceSummary(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("출결 요약 조회 성공",
                attendanceService.getAttendanceSummary(studentId)));
    }

    // 출결 수정
    @PutMapping("/api/v1/attendance/{attendanceId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "출결 수정", description = "출결 기록 수정 [TEACHER, ADMIN]")
    public ResponseEntity<ApiResponse<Void>> updateAttendance(
            @PathVariable Long attendanceId,
            @Valid @RequestBody AttendanceUpdateRequest request) {
        attendanceService.updateAttendance(attendanceId, request);
        return ResponseEntity.ok(ApiResponse.success("출결 수정 성공"));
    }

    // 출결 삭제
    @DeleteMapping("/api/v1/attendance/{attendanceId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "출결 삭제", description = "출결 기록 삭제 (soft delete) [TEACHER, ADMIN]")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(
            @PathVariable Long attendanceId) {
        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity.ok(ApiResponse.success("출결 삭제 성공"));
    }
}