package com.school.management.domain.record.service;

import com.school.management.domain.auth.entity.User;
import com.school.management.domain.auth.repository.UserRepository;
import com.school.management.domain.record.dto.*;
import com.school.management.domain.record.entity.StudentRecord;
import com.school.management.domain.record.repository.StudentRecordRepository;
import com.school.management.domain.student.entity.Student;
import com.school.management.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentRecordService {

    private final StudentRecordRepository studentRecordRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    // 학생부 항목 추가
    @Transactional
    public void createRecord(Long studentId, Long teacherId, StudentRecordCreateRequest request) {
        Student student = studentRepository.findByIdAndIsDeletedFalse(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        StudentRecord record = new StudentRecord(
                student,
                teacher,
                request.getCategory(),
                request.getNote(),
                request.getRecordDate(),
                request.getSemester()
        );
        studentRecordRepository.save(record);
    }

    // 학생부 조회
    @Transactional(readOnly = true)
    public List<StudentRecordResponse> getRecords(Long studentId) {
        studentRepository.findByIdAndIsDeletedFalse(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        return studentRecordRepository.findAllByStudentIdAndIsDeletedFalse(studentId)
                .stream()
                .map(StudentRecordResponse::new)
                .toList();
    }

    // 학생부 수정
    @Transactional
    public void updateRecord(Long recordId, StudentRecordUpdateRequest request) {
        StudentRecord record = studentRecordRepository.findByIdAndIsDeletedFalse(recordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생부 항목입니다."));

        record.update(
                request.getCategory(),
                request.getNote(),
                request.getRecordDate(),
                request.getSemester()
        );
    }

    // 학생부 삭제
    @Transactional
    public void deleteRecord(Long recordId) {
        StudentRecord record = studentRecordRepository.findByIdAndIsDeletedFalse(recordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생부 항목입니다."));

        record.delete();
    }
}