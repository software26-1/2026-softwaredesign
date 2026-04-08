package com.school.management.domain.attendance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AttendanceUpdateRequest {

    @NotNull
    private LocalDate attendanceDate;

    @NotNull
    private Boolean isAttended;

    private String reason;

    private String note;
}