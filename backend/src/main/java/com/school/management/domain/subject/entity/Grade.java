package com.school.management.domain.subject.entity;

import com.school.management.domain.student.entity.Student;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.math.BigDecimal;

/**
 * 학생 성적 엔티티 (2015 개정 교육과정 기준)
 *
 * [grade_type별 필드 사용 규칙]
 *
 *   RELATIVE (고등학교 공통과목 / 일반선택 — 체육·예술 제외)
 *     - rawScore        : 원점수 (필수)
 *     - achievementLevel: 성취도 A~E (필수)
 *     - rankGrade       : 석차등급 1~9 (필수)
 *       └ 석차등급 산출: 수강자 수 × 누적비율로 자동 계산
 *         1등급(4%) 2(11%) 3(23%) 4(40%) 5(60%) 6(77%) 7(89%) 8(96%) 9(100%)
 *
 *   ABSOLUTE (중학교 전 과목 / 고등학교 진로선택·체육·예술)
 *     - rawScore        : 원점수 (필수)
 *     - achievementLevel: 성취도 (필수)
 *       └ 일반: A(90↑) B(80↑) C(70↑) D(60↑) E(60↓)
 *       └ 체육·예술: A(80↑) B(60↑) C(60↓)
 *     - rankGrade       : null 고정
 *
 *   PASS_FAIL (고등학교 교양)
 *     - achievementLevel: "P"(이수) 또는 "F"(미이수) 저장
 *     - rawScore        : null 가능
 *     - rankGrade       : null 고정
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "grade")
public class Grade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학생 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // 반 전체 통계 FK — 석차등급 계산 및 성취도 비율 기준으로 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_statistic_id", nullable = false)
    private ClassStatistic classStatistic;

    // 원점수 (100점 만점)
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal rawScore;

    // 성취도: A~E (일반), A~C (체육·예술), P/F (교양)
    @Column(nullable = false)
    private String achievementLevel;

    // 석차등급 1~9 — RELATIVE 과목만 사용, ABSOLUTE·PASS_FAIL은 null
    @Column
    private Integer rankGrade;

    public Grade(Student student, ClassStatistic classStatistic,
                 BigDecimal rawScore, String achievementLevel, Integer rankGrade) {
        this.student = student;
        this.classStatistic = classStatistic;
        this.rawScore = rawScore;
        this.achievementLevel = achievementLevel;
        this.rankGrade = rankGrade;
    }

    public void update(BigDecimal rawScore, String achievementLevel, Integer rankGrade) {
        this.rawScore = rawScore;
        this.achievementLevel = achievementLevel;
        this.rankGrade = rankGrade;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
