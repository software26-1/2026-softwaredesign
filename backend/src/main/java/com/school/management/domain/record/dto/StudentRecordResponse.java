package com.school.management.domain.record.dto;

import com.school.management.domain.record.entity.StudentRecord;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StudentRecordResponse {

    private final Long id;
    private final Long studentId;
    private final Long createdById;
    private final String createdByName;
    private final String category;
    private final String note;
    private final LocalDate recordDate;
    private final String semester;

    public StudentRecordResponse(StudentRecord record) {
        this.id = record.getId();
        this.studentId = record.getStudent().getId();
        this.createdById = record.getCreatedBy().getId();
        this.createdByName = record.getCreatedBy().getName();
        this.category = record.getCategory();
        this.note = record.getNote();
        this.recordDate = record.getRecordDate();
        this.semester = record.getSemester();
    }
}