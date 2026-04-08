package com.school.management.domain.subject.repository;

import com.school.management.domain.subject.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    // 학생별 전체 성적 조회
    List<Grade> findAllByStudentIdAndIsDeletedFalse(Long studentId);

    // 학생 + 과목 통계로 성적 조회
    Optional<Grade> findByStudentIdAndClassStatisticIdAndIsDeletedFalse(
            Long studentId, Long classStatisticId);

    // 같은 반 + 과목 통계 기준 전체 성적 (석차등급 계산용)
    List<Grade> findAllByClassStatisticIdAndIsDeletedFalse(Long classStatisticId);

    // 학생 + 학기 기준 성적 조회 (레이더 차트용)
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.classStatistic.semester = :semester AND g.isDeleted = false")
    List<Grade> findAllByStudentIdAndSemesterAndIsDeletedFalse(@Param("studentId") Long studentId, @Param("semester") String semester);
}
