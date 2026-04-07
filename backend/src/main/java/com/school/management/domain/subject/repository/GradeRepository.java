package com.school.management.domain.subject.repository;

import com.school.management.domain.subject.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    // 학생별 전체 성적 조회
    List<Grade> findAllByStudentIdAndIsDeletedFalse(Long studentId);

    // 학생 + 과목 통계로 성적 조회
    Optional<Grade> findByStudentIdAndClassStatisticIdAndIsDeletedFalse(
            Long studentId, Long classStatisticId);
}
