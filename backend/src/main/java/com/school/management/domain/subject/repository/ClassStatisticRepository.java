package com.school.management.domain.subject.repository;

import com.school.management.domain.subject.entity.ClassStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassStatisticRepository extends JpaRepository<ClassStatistic, Long> {

    // 과목 + 학기로 통계 조회
    Optional<ClassStatistic> findBySubjectIdAndSemesterAndIsDeletedFalse(Long subjectId, String semester);

    // 과목별 전체 통계 조회
    List<ClassStatistic> findAllBySubjectIdAndIsDeletedFalse(Long subjectId);
}
