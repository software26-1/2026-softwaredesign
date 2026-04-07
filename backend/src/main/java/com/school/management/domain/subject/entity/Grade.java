package com.school.management.domain.subject.entity;

import com.school.management.domain.student.entity.Student;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "grade")
public class Grade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학생 (student 테이블 FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // 과목 통계 (class_statistic 테이블 FK)
    // 등급 계산에 필요한 반 전체 통계 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_statistic_id", nullable = false)
    private ClassStatistic classStatistic;

    // 원점수
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal rawScore;

    // 성취도 (A, B, C, D, E)
    @Column(nullable = false)
    private String achievementLevel;

    public Grade(Student student, ClassStatistic classStatistic,
                 BigDecimal rawScore, String achievementLevel) {
        this.student = student;
        this.classStatistic = classStatistic;
        this.rawScore = rawScore;
        this.achievementLevel = achievementLevel;
    }

    // 성적 수정
    public void update(BigDecimal rawScore, String achievementLevel) {
        this.rawScore = rawScore;
        this.achievementLevel = achievementLevel;
    }

    // 소프트 삭제
    public void delete() {
        this.isDeleted = true;
    }
}
