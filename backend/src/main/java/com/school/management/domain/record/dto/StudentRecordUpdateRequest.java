package com.school.management.domain.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StudentRecordUpdateRequest {

    @NotBlank
    private String category;

    private String note;

    @NotNull
    private LocalDate recordDate;

    @NotBlank
    private String semester;
}