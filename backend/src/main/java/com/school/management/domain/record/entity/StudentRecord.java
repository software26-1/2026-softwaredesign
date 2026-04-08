package com.school.management.domain.record.entity;

import com.school.management.domain.auth.entity.User;
import com.school.management.domain.student.entity.Student;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "student_record")
public class StudentRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // 작성 교사
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column(nullable = false, length = 10)
    private String semester;

    public StudentRecord(Student student, User createdBy, String category,
                         String note, LocalDate recordDate, String semester) {
        this.student = student;
        this.createdBy = createdBy;
        this.category = category;
        this.note = note;
        this.recordDate = recordDate;
        this.semester = semester;
    }

    public void update(String category, String note,
                       LocalDate recordDate, String semester) {
        this.category = category;
        this.note = note;
        this.recordDate = recordDate;
        this.semester = semester;
    }

    public void delete() {
        this.isDeleted = true;
    }
}