package com.k_plus.internship.StatisticsPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Name;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StatRequestDto {

    @JsonProperty("course_id")
    private UUID courseId;

    @JsonProperty("user_id")
    private UUID userId;
}
