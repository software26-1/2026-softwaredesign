package com.school.management.domain.subject.dto;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class GradeCreateRequest {
    private Long studentId;
    private Long classStatisticId;
    private BigDecimal rawScore;
}
