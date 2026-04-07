package com.school.management.domain.subject.repository;

import com.school.management.domain.subject.entity.SemesterSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterSummaryRepository extends JpaRepository<SemesterSummary, Long> {

    // 학생의 전체 학기 요약 조회
    List<SemesterSummary> findAllByStudentIdAndIsDeletedFalse(Long studentId);

    // 학생 + 학기로 요약 조회
    Optional<SemesterSummary> findByStudentIdAndSemesterAndIsDeletedFalse(
            Long studentId, String semester);
}
