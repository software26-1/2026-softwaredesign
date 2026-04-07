package com.school.management.domain.subject.controller;

import com.google.protobuf.Api;
import com.school.management.domain.subject.dto.GradeCreateRequest;
import com.school.management.domain.subject.entity.Grade;
import com.school.management.domain.subject.entity.SemesterSummary;
import com.school.management.domain.subject.service.GradeService;
import com.school.management.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    // 성적 등록
    // GradeCreateRequest를 만들어서 민감한 정보를 URL에 노출하는 것을 방지
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> registerGrade(
            @RequestBody GradeCreateRequest request) {
        gradeService.registerGrade(
                request.getStudentId(),
                request.getClassStatisticId(),
                request.getRawScore()
        );
        return ResponseEntity.ok(ApiResponse.success("성적 등록 성공"));
    }

    // 성적 조회
    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<Grade>>> getStudentGrades(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("성적 조회 성공",
                gradeService.getStudentGrades(studentId)));
    }

    // 학기 요약 조회
    @GetMapping("/students/{studentId}/summary")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT', 'PARENT')")
    public ResponseEntity<ApiResponse<List<SemesterSummary>>> getStudentSemesterSummaries(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("학기 요약 조회 성공",
                gradeService.getStudentSemesterSummaries(studentId)));
    }
}
