package com.school.management.domain.attendance.service;

import com.school.management.domain.attendance.dto.*;
import com.school.management.domain.attendance.entity.Attendance;
import com.school.management.domain.attendance.repository.AttendanceRepository;
import com.school.management.domain.student.entity.Student;
import com.school.management.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    // 출결 등록
    @Transactional
    public void createAttendance(AttendanceCreateRequest request) {
        Student student = studentRepository.findByIdAndIsDeletedFalse(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        Attendance attendance = new Attendance(
                student,
                request.getAttendanceDate(),
                request.getIsAttended(),
                request.getReason(),
                request.getNote()
        );
        attendanceRepository.save(attendance);
    }

    // 출결 내역 조회
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendances(Long studentId) {
        studentRepository.findByIdAndIsDeletedFalse(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        return attendanceRepository.findAllByStudentIdAndIsDeletedFalse(studentId)
                .stream()
                .map(AttendanceResponse::new)
                .toList();
    }

    // 출결 요약 조회
    @Transactional(readOnly = true)
    public AttendanceSummaryResponse getAttendanceSummary(Long studentId) {
        studentRepository.findByIdAndIsDeletedFalse(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        List<Attendance> attendances = attendanceRepository.findAllByStudentIdAndIsDeletedFalse(studentId);
        int totalDays = attendances.size();
        int attendedDays = (int) attendances.stream().filter(Attendance::getIsAttended).count();

        return new AttendanceSummaryResponse(totalDays, attendedDays);
    }

    // 출결 수정
    @Transactional
    public void updateAttendance(Long attendanceId, AttendanceUpdateRequest request) {
        Attendance attendance = attendanceRepository.findByIdAndIsDeletedFalse(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출결 기록입니다."));

        attendance.update(
                request.getAttendanceDate(),
                request.getIsAttended(),
                request.getReason(),
                request.getNote()
        );
    }

    // 출결 삭제
    @Transactional
    public void deleteAttendance(Long attendanceId) {
        Attendance attendance = attendanceRepository.findByIdAndIsDeletedFalse(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출결 기록입니다."));

        attendance.delete();
    }
}