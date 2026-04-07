package com.school.management.domain.student.controller;

import com.school.management.domain.student.dto.StudentCreateRequest;
import com.school.management.domain.student.dto.StudentResponse;
import com.school.management.domain.student.dto.StudentUpdateRequest;
import com.school.management.domain.student.service.StudentService;
import com.school.management.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    // 전체 학생 조회
    // GET /api/students
    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents() {
        return ResponseEntity.ok(ApiResponse.success("학생 목록 조회 성공", studentService.getAllStudents()));
    }

    // 특정 학생 조회
    // GET /api/students/1
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("학생 조회 성공", studentService.getStudent(id)));
    }

    // 학생 등록
    // POST /api/students
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(@RequestBody StudentCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("학생 등록 성공", studentService.createStudent(request)));
    }

    // 학생 수정
    // PUT /api/students/1
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("학생 수정 성공", studentService.updateStudent(id, request)));
    }

    // 학생 삭제
    // DELETE /api/students/1
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("학생 삭제 성공"));
    }
}
