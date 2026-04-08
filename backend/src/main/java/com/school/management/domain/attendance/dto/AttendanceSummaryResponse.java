package com.school.management.domain.attendance.dto;

import lombok.Getter;

@Getter
public class AttendanceSummaryResponse {

    private final int totalDays;
    private final int attendedDays;
    private final int absentDays;

    public AttendanceSummaryResponse(int totalDays, int attendedDays) {
        this.totalDays = totalDays;
        this.attendedDays = attendedDays;
        this.absentDays = totalDays - attendedDays;
    }
}