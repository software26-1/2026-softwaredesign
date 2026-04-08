package com.school.management.domain.subject.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public GradeRadarResponse(
            @JsonProperty("subjectName") String subjectName,
            @JsonProperty("achievementLevel") String achievementLevel,
            @JsonProperty("rankGrade") Integer rankGrade) {
        this.subjectName = subjectName;
        this.achievementLevel = achievementLevel;
        this.rankGrade = rankGrade;
    }
}
