package com.school.management.domain.attendance.repository;

import com.school.management.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByStudentIdAndIsDeletedFalse(Long studentId);

    Optional<Attendance> findByIdAndIsDeletedFalse(Long id);
}