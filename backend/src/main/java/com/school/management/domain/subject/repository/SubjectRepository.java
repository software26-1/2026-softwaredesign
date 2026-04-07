package com.school.management.domain.subject.repository;

import com.school.management.domain.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // 삭제 안 된 전체 과목 조회
    List<Subject> findAllByIsDeletedFalse();

    // 특정 과목 조회
    Optional<Subject> findByIdAndIsDeletedFalse(Long id);

    // 교사별 과목 조회
    List<Subject> findAllByTeacherIdAndIsDeletedFalse(Long teacherId);
}
