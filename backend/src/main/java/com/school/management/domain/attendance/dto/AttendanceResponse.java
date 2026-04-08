package com.school.management.domain.attendance.dto;

import com.school.management.domain.attendance.entity.Attendance;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AttendanceResponse {

    private final Long id;
    private final Long studentId;
    private final LocalDate attendanceDate;
    private final Boolean isAttended;
    private final String reason;
    private final String note;

    public AttendanceResponse(Attendance attendance) {
        this.id = attendance.getId();
        this.studentId = attendance.getStudent().getId();
        this.attendanceDate = attendance.getAttendanceDate();
        this.isAttended = attendance.getIsAttended();
        this.reason = attendance.getReason();
        this.note = attendance.getNote();
    }
}