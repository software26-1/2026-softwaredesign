package com.school.management.domain.subject.entity;

import com.school.management.domain.auth.entity.User;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "class_statistic")
public class ClassStatistic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private Integer enrolledCount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal averageScore;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal standardDev;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal ratioA;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal ratioB;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal ratioC;

    public ClassStatistic(User teacher, Subject subject, String semester,
                          Integer enrolledCount, BigDecimal averageScore,
                          BigDecimal standardDev, BigDecimal ratioA,
                          BigDecimal ratioB, BigDecimal ratioC) {
        this.teacher = teacher;
        this.subject = subject;
        this.semester = semester;
        this.enrolledCount = enrolledCount;
        this.averageScore = averageScore;
        this.standardDev = standardDev;
        this.ratioA = ratioA;
        this.ratioB = ratioB;
        this.ratioC = ratioC;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
