package com.school.management.domain.subject.entity;

import com.school.management.domain.auth.entity.User;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 교과목 엔티티 (2015 개정 교육과정 기준)
 *
 * [school_type]
 *   MIDDLE : 중학교
 *   HIGH   : 고등학교
 *
 * [category] 과목 분류
 *   중학교  : 국어, 수학, 영어, 사회, 역사, 도덕, 과학, 기술·가정, 정보, 체육, 음악, 미술, 외국어, 한문 등
 *   고등학교: 공통과목, 일반선택, 진로선택, 교양
 *             ※ 체육·예술은 일반선택 내 별도 구분 (isCareerElective 와 함께 판단)
 *
 * [grade_type] 평가 방식 — 2015 개정 교육과정 기준
 *
 *   RELATIVE  (상대평가)
 *     - 고등학교 공통과목 + 일반선택 (단, 체육·예술 제외)
 *     - 석차등급(1~9등급) + 성취도(A~E) 모두 산출
 *     - 나이스 입력항목: 원점수, 과목평균, 표준편차, 수강자수, 석차등급, 성취도
 *     - 석차등급 비율: 1(4%) 2(7%) 3(12%) 4(17%) 5(20%) 6(17%) 7(12%) 8(7%) 9(4%)
 *
 *   ABSOLUTE  (절대평가 — 성취도만)
 *     - 중학교 전 과목
 *     - 고등학교 진로선택 과목
 *     - 고등학교 체육·예술 과목
 *     - 성취도만 산출 (석차등급 없음)
 *       · 일반 과목: A(90%↑) B(80%↑) C(70%↑) D(60%↑) E(60%↓)
 *       · 체육·예술: A(80%↑) B(60%↑) C(60%↓)  ← 3단계만 사용
 *     - 나이스 입력항목: 원점수, 과목평균, 표준편차, 수강자수, 성취도
 *
 *   PASS_FAIL (이수/미이수)
 *     - 고등학교 교양 과목
 *     - 석차등급·성취도 없음, 이수 여부만 기록
 *     - 나이스 입력항목: 이수/미이수
 *
 * [teacher_id]
 *   - V10 seed 데이터(시스템 기본 과목)는 teacher_id = null
 *   - 교사가 직접 추가한 과목은 teacher_id 연결
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "subject")
public class Subject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시스템 기본 과목(seed)은 null, 교사 직접 추가 과목은 FK 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    // 과목 분류 (국어 / 수학 / 공통과목 / 일반선택 / 진로선택 / 교양 등)
    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false)
    private String subjectName;

    // 단위수(학점)
    @Column(nullable = false)
    private Integer credits;

    // 진로선택 여부 — true이면 ABSOLUTE 평가 (고등학교)
    @Column(nullable = false)
    private boolean isCareerElective = false;

    // MIDDLE / HIGH
    @Column(nullable = false, length = 10)
    private String schoolType;

    // RELATIVE / ABSOLUTE / PASS_FAIL (클래스 주석 참고)
    @Column(nullable = false, length = 20)
    private String gradeType;

    public Subject(User teacher, String category, String subjectName,
                   Integer credits, boolean isCareerElective,
                   String schoolType, String gradeType) {
        this.teacher = teacher;
        this.category = category;
        this.subjectName = subjectName;
        this.credits = credits;
        this.isCareerElective = isCareerElective;
        this.schoolType = schoolType;
        this.gradeType = gradeType;
    }

    public void update(String category, String subjectName,
                       Integer credits, boolean isCareerElective,
                       String schoolType, String gradeType) {
        this.category = category;
        this.subjectName = subjectName;
        this.credits = credits;
        this.isCareerElective = isCareerElective;
        this.schoolType = schoolType;
        this.gradeType = gradeType;
    }

    public void delete() {
        this.isDeleted = true;
    }
}