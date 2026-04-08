package com.school.management.domain.record.repository;

import com.school.management.domain.record.entity.StudentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRecordRepository extends JpaRepository<StudentRecord, Long> {

    List<StudentRecord> findAllByStudentIdAndIsDeletedFalse(Long studentId);

    Optional<StudentRecord> findByIdAndIsDeletedFalse(Long id);
}