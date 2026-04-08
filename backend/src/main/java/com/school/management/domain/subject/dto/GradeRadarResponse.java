package com.school.management.domain.subject.dto;

import com.school.management.domain.subject.entity.Grade;
import lombok.Getter;

@Getter
public class GradeRadarResponse {

    private final String subjectName;
    private final String achievementLevel;
    private final Integer rankGrade;

    public GradeRadarResponse(Grade grade) {
        this.subjectName = grade.getClassStatistic().getSubject().getSubjectName();
        this.achievementLevel = grade.getAchievementLevel();
        this.rankGrade = grade.getRankGrade();
    }
}
