package com.k_plus.internship.CoursePackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SubscribeRequestDto {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("course_id")
    private UUID courseId;
}
