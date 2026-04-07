package com.school.management.domain.subject.entity;

import com.school.management.domain.auth.entity.User;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 반 전체 성적 통계 엔티티 (2015 개정 교육과정 기준)
 *
 * 나이스(NEIS)에서 교사가 과목별로 입력하는 반 전체 통계값.
 * 이 값을 기반으로 개별 학생의 석차등급·성취도를 산출한다.
 *
 * [필수 입력 항목 — 과목 유형 공통]
 *   - semester      : 학기 (예: "2024-1", "2024-2")
 *   - enrolledCount : 수강자 수 (석차등급 산출 기준)
 *   - averageScore  : 과목 평균 (소수점 첫째자리, 나이스 기재)
 *   - standardDev   : 표준편차 (소수점 첫째자리, 나이스 기재)
 *
 * [성취도 비율 — grade_type별 사용 규칙]
 *
 *   RELATIVE (고등학교 공통과목 / 일반선택 — 체육·예술 제외)
 *     - ratioA~ratioE 모두 사용 (성취도 A~E 각 등급 비율 %)
 *     - 석차등급은 enrolledCount × 누적비율로 자동 계산
 *
 *   ABSOLUTE — 일반 (중학교 전 과목 / 고등학교 진로선택)
 *     - ratioA~ratioE 사용 (성취도 A~E 비율 %)
 *     - ratioD, ratioE: 해당 없으면 0.00 입력
 *
 *   ABSOLUTE — 체육·예술 (3단계 성취도)
 *     - ratioA, ratioB, ratioC만 사용
 *     - ratioD, ratioE = null
 *
 *   PASS_FAIL (고등학교 교양)
 *     - 모든 ratio = null (이수/미이수만 기록)
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "class_statistic")
public class ClassStatistic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 통계를 입력한 담당 교사
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    // 해당 교과목 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // 학기 (예: "2024-1")
    @Column(nullable = false)
    private String semester;

    // 수강자 수 — 석차등급 산출 시 분모로 사용
    @Column(nullable = false)
    private Integer enrolledCount;

    // 과목 평균 (소수점 첫째자리까지, 나이스 기재 항목)
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal averageScore;

    // 표준편차 (소수점 첫째자리까지, 나이스 기재 항목)
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal standardDev;

    // 성취도 A 비율 (%) — 중학교·고등학교 공통 사용
    @Column(name = "ratio_a", nullable = false, precision = 5, scale = 2)
    private BigDecimal ratioA;

    // 성취도 B 비율 (%)
    @Column(name = "ratio_b", nullable = false, precision = 5, scale = 2)
    private BigDecimal ratioB;

    // 성취도 C 비율 (%)
    @Column(name = "ratio_c", nullable = false, precision = 5, scale = 2)
    private BigDecimal ratioC;

    // 성취도 D 비율 (%) — RELATIVE·ABSOLUTE 일반 과목만 사용, 체육·예술·PASS_FAIL은 null
    @Column(name = "ratio_d", precision = 5, scale = 2)
    private BigDecimal ratioD;

    // 성취도 E 비율 (%) — RELATIVE·ABSOLUTE 일반 과목만 사용, 체육·예술·PASS_FAIL은 null
    @Column(name = "ratio_e", precision = 5, scale = 2)
    private BigDecimal ratioE;

    public ClassStatistic(User teacher, Subject subject, String semester,
                          Integer enrolledCount, BigDecimal averageScore,
                          BigDecimal standardDev, BigDecimal ratioA,
                          BigDecimal ratioB, BigDecimal ratioC,
                          BigDecimal ratioD, BigDecimal ratioE) {
        this.teacher = teacher;
        this.subject = subject;
        this.semester = semester;
        this.enrolledCount = enrolledCount;
        this.averageScore = averageScore;
        this.standardDev = standardDev;
        this.ratioA = ratioA;
        this.ratioB = ratioB;
        this.ratioC = ratioC;
        this.ratioD = ratioD;
        this.ratioE = ratioE;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
