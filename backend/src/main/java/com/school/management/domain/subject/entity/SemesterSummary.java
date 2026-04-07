package com.school.management.domain.subject.entity;

import com.school.management.domain.student.entity.Student;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "semester_summary")
public class SemesterSummary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private Integer totalCredits;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal totalScore;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal averageScore;

    @Column
    private Integer ranking;

    public SemesterSummary(Student student, String semester, Integer totalCredits,
                           BigDecimal totalScore, BigDecimal averageScore, Integer ranking) {
        this.student = student;
        this.semester = semester;
        this.totalCredits = totalCredits;
        this.totalScore = totalScore;
        this.averageScore = averageScore;
        this.ranking = ranking;
    }

    public void update(Integer totalCredits, BigDecimal totalScore,
                       BigDecimal averageScore, Integer ranking) {
        this.totalCredits = totalCredits;
        this.totalScore = totalScore;
        this.averageScore = averageScore;
        this.ranking = ranking;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
