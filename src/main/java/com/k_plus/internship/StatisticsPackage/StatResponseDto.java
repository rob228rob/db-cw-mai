package com.k_plus.internship.StatisticsPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class StatResponseDto {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("course_id")
    private UUID courseId;

    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("rank")
    private long rank;

    @JsonProperty("percentage_correct")
    private double percentageCorrect;

    public boolean isCourseCompleted() {
        return percentageCorrect - 80.0 > 0.0001;
    }
}
