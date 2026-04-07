package com.school.management.domain.student.service;

import com.school.management.domain.auth.entity.User;
import com.school.management.domain.auth.repository.UserRepository;
import com.school.management.domain.student.dto.StudentCreateRequest;
import com.school.management.domain.student.dto.StudentResponse;
import com.school.management.domain.student.dto.StudentUpdateRequest;
import com.school.management.domain.student.entity.Student;
import com.school.management.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    // 전체 학생 조회
    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAllByIsDeletedFalse()
                .stream()
                .map(StudentResponse::from)
                .collect(Collectors.toList());
    }

    // 특정 학생 조회
    @Transactional(readOnly = true)
    public StudentResponse getStudent(Long id) {
        Student student = studentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));
        return StudentResponse.from(student);
    }

    // 학생 등록
    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Student student = new Student(
                user,
                request.getName(),
                request.getGrade(),
                request.getClassNum(),
                request.getNumber()
        );

        return StudentResponse.from(studentRepository.save(student));
    }

    // 학생 수정
    @Transactional
    public StudentResponse updateStudent(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        student.update(request.getName(), request.getGrade(), request.getClassNum(), request.getNumber());

        return StudentResponse.from(student);
    }

    // 학생 삭제
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        student.delete();
    }
}
