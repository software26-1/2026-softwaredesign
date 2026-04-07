package com.school.management.domain.subject.entity;

import com.school.management.domain.auth.entity.User;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "subject")
public class Subject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false)
    private boolean isCareerElective = false;

    public Subject(User teacher, String category, String subjectName,
                   Integer credits, boolean isCareerElective) {
        this.teacher = teacher;
        this.category = category;
        this.subjectName = subjectName;
        this.credits = credits;
        this.isCareerElective = isCareerElective;
    }

    public void update(String category, String subjectName,
                       Integer credits, boolean isCareerElective) {
        this.category = category;
        this.subjectName = subjectName;
        this.credits = credits;
        this.isCareerElective = isCareerElective;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
