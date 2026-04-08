package com.school.management.domain.subject.service;

import com.school.management.domain.student.entity.Student;
import com.school.management.domain.student.repository.StudentRepository;
import com.school.management.domain.subject.entity.ClassStatistic;
import com.school.management.domain.subject.entity.Grade;
import com.school.management.domain.subject.entity.SemesterSummary;
import com.school.management.domain.subject.dto.GradeRadarResponse;
import com.school.management.domain.subject.repository.ClassStatisticRepository;
import com.school.management.domain.subject.repository.GradeRepository;
import com.school.management.domain.subject.repository.SemesterSummaryRepository;
import com.school.management.domain.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final ClassStatisticRepository classStatisticRepository;
    private final SemesterSummaryRepository semesterSummaryRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    @CacheEvict(value = "grades:radar", allEntries = true)
    public void registerGrade(Long studentId, Long classStatisticId, BigDecimal rawScore) {

        Student student = studentRepository.findByIdAndIsDeletedFalse(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        ClassStatistic classStatistic = classStatisticRepository.findById(classStatisticId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목 통계입니다."));

        String achievementLevel = calculateAchievementLevel(rawScore);

        Integer rankGrade = null;
        if ("RELATIVE".equals(classStatistic.getSubject().getGradeType())) {
            rankGrade = calculateRankGrade(rawScore, classStatistic);
        }

        Grade grade = new Grade(student, classStatistic, rawScore, achievementLevel, rankGrade);
        gradeRepository.save(grade);

        updateSemesterSummary(student, classStatistic.getSemester());
    }

    // 성취도 자동 계산 (2015 개정 교육과정 기준 — 원점수 기준 고정 비율)
    // A: 90점 이상, B: 80점 이상, C: 70점 이상, D: 60점 이상, E: 60점 미만
    private String calculateAchievementLevel(BigDecimal rawScore) {
        int score = rawScore.intValue();
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "E";
    }

    // 석차등급 자동 계산 (RELATIVE 과목 전용 — 2015 개정 교육과정 9등급제)
    // 석차 = (나보다 높은 점수 수강자 수) + 1
    // 석차등급 누적비율: 1(4%) 2(11%) 3(23%) 4(40%) 5(60%) 6(77%) 7(89%) 8(96%) 9(100%)
    private Integer calculateRankGrade(BigDecimal rawScore, ClassStatistic stat) {
        int enrolled = stat.getEnrolledCount();
        List<Grade> classGrades = gradeRepository.findAllByClassStatisticIdAndIsDeletedFalse(stat.getId());

        long rank = classGrades.stream()
                .filter(g -> g.getRawScore().compareTo(rawScore) > 0)
                .count() + 1;

        double rankRatio = (double) rank / enrolled * 100;

        if (rankRatio <= 4) return 1;
        if (rankRatio <= 11) return 2;
        if (rankRatio <= 23) return 3;
        if (rankRatio <= 40) return 4;
        if (rankRatio <= 60) return 5;
        if (rankRatio <= 77) return 6;
        if (rankRatio <= 89) return 7;
        if (rankRatio <= 96) return 8;
        return 9;
    }

    // 학기 요약 자동 업데이트 — 성적 등록/수정/삭제 시 자동 호출
    private void updateSemesterSummary(Student student, String semester) {

        List<Grade> grades = gradeRepository.findAllByStudentIdAndIsDeletedFalse(student.getId())
                .stream()
                .filter(g -> g.getClassStatistic().getSemester().equals(semester))
                .toList();

        if (grades.isEmpty()) return;

        // 총점 / 총 이수단위 / 평균 원점수
        BigDecimal totalScore = grades.stream()
                .map(Grade::getRawScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalCredits = grades.stream()
                .mapToInt(g -> g.getClassStatistic().getSubject().getCredits())
                .sum();

        BigDecimal averageScore = totalScore.divide(
                BigDecimal.valueOf(grades.size()), 2, RoundingMode.HALF_UP);

        // 고등학교 평균 석차등급: Σ(석차등급 × 이수단위) / Σ(이수단위)
        // RELATIVE 과목(공통/일반선택)만 포함, 중학교는 null
        BigDecimal averageRankGrade = null;
        List<Grade> relativeGrades = grades.stream()
                .filter(g -> "RELATIVE".equals(g.getClassStatistic().getSubject().getGradeType())
                        && g.getRankGrade() != null)
                .toList();

        if (!relativeGrades.isEmpty()) {
            int weightedSum = relativeGrades.stream()
                    .mapToInt(g -> g.getRankGrade() * g.getClassStatistic().getSubject().getCredits())
                    .sum();
            int relativeCredits = relativeGrades.stream()
                    .mapToInt(g -> g.getClassStatistic().getSubject().getCredits())
                    .sum();
            averageRankGrade = BigDecimal.valueOf(weightedSum)
                    .divide(BigDecimal.valueOf(relativeCredits), 2, RoundingMode.HALF_UP);
        }

        SemesterSummary summary = semesterSummaryRepository
                .findByStudentIdAndSemesterAndIsDeletedFalse(student.getId(), semester)
                .orElse(null);

        if (summary != null) {
            summary.update(totalCredits, totalScore, averageScore, null, averageRankGrade);
        } else {
            semesterSummaryRepository.save(
                    new SemesterSummary(student, semester, totalCredits, totalScore, averageScore, null, averageRankGrade)
            );
        }
    }

    // 성적 수정 — 원점수 변경 시 성취도·석차등급·학기요약 자동 재계산
    @Transactional
    @CacheEvict(value = "grades:radar", allEntries = true)
    public void updateGrade(Long gradeId, BigDecimal rawScore) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 성적입니다."));

        String achievementLevel = calculateAchievementLevel(rawScore);

        Integer rankGrade = null;
        if ("RELATIVE".equals(grade.getClassStatistic().getSubject().getGradeType())) {
            rankGrade = calculateRankGrade(rawScore, grade.getClassStatistic());
        }

        grade.update(rawScore, achievementLevel, rankGrade);
        updateSemesterSummary(grade.getStudent(), grade.getClassStatistic().getSemester());
    }

    // 성적 삭제 (soft delete) — 삭제 후 학기 요약 재계산
    @Transactional
    @CacheEvict(value = "grades:radar", allEntries = true)
    public void deleteGrade(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 성적입니다."));

        grade.delete();
        updateSemesterSummary(grade.getStudent(), grade.getClassStatistic().getSemester());
    }

    // 학생 성적 조회
    @Transactional(readOnly = true)
    public List<Grade> getStudentGrades(Long studentId) {
        return gradeRepository.findAllByStudentIdAndIsDeletedFalse(studentId);
    }

    // 학기 요약 조회
    @Transactional(readOnly = true)
    public List<SemesterSummary> getStudentSemesterSummaries(Long studentId) {
        return semesterSummaryRepository.findAllByStudentIdAndIsDeletedFalse(studentId);
    }

    // 레이더 차트용 과목별 성적 조회 (학기별, Redis 캐싱)
    @Transactional(readOnly = true)
    @Cacheable(value = "grades:radar", key = "#studentId + ':' + #semester")
    public List<GradeRadarResponse> getStudentRadarGrades(Long studentId, String semester) {
        return gradeRepository.findAllByStudentIdAndSemesterAndIsDeletedFalse(studentId, semester)
                .stream()
                .map(GradeRadarResponse::new)
                .toList();
    }
}
