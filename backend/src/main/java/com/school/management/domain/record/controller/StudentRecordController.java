package com.school.management.domain.record.controller;

import com.school.management.domain.record.dto.*;
import com.school.management.domain.record.service.StudentRecordService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "StudentRecord", description = "학생부 관련 API")
public class StudentRecordController {

    private final StudentRecordService studentRecordService;

    // 학생부 항목 추가
    @PostMapping("/api/v1/students/{studentId}/records")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "학생부 항목 추가", description = "학생부 항목 추가 [TEACHER, ADMIN]")
    public ResponseEntity<ApiResponse<Void>> createRecord(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentRecordCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long teacherId = Long.parseLong(userDetails.getUsername());
        studentRecordService.createRecord(studentId, teacherId, request);
        return ResponseEntity.ok(ApiResponse.success("학생부 항목 추가 성공"));
    }

    // 학생부 조회
    @GetMapping("/api/v1/students/{studentId}/records")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT', 'PARENT')")
    @Operation(summary = "학생부 조회", description = "학생별 학생부 전체 조회")
    public ResponseEntity<ApiResponse<List<StudentRecordResponse>>> getRecords(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("학생부 조회 성공",
                studentRecordService.getRecords(studentId)));
    }

    // 학생부 수정
    @PutMapping("/api/v1/records/{recordId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "학생부 수정", description = "학생부 항목 수정 [TEACHER, ADMIN]")
    public ResponseEntity<ApiResponse<Void>> updateRecord(
            @PathVariable Long recordId,
            @Valid @RequestBody StudentRecordUpdateRequest request) {
        studentRecordService.updateRecord(recordId, request);
        return ResponseEntity.ok(ApiResponse.success("학생부 수정 성공"));
    }

    // 학생부 삭제
    @DeleteMapping("/api/v1/records/{recordId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "학생부 삭제", description = "학생부 항목 삭제 (soft delete) [TEACHER, ADMIN]")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(
            @PathVariable Long recordId) {
        studentRecordService.deleteRecord(recordId);
        return ResponseEntity.ok(ApiResponse.success("학생부 삭제 성공"));
    }
}