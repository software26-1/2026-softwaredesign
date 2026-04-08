package com.school.management.domain.attendance.entity;

import com.school.management.domain.student.entity.Student;
import com.school.management.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false)
    private Boolean isAttended;

    @Column(length = 100)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String note;

    public Attendance(Student student, LocalDate attendanceDate,
                      Boolean isAttended, String reason, String note) {
        this.student = student;
        this.attendanceDate = attendanceDate;
        this.isAttended = isAttended;
        this.reason = reason;
        this.note = note;
    }

    public void update(LocalDate attendanceDate, Boolean isAttended,
                       String reason, String note) {
        this.attendanceDate = attendanceDate;
        this.isAttended = isAttended;
        this.reason = reason;
        this.note = note;
    }

    public void delete() {
        this.isDeleted = true;
    }
}