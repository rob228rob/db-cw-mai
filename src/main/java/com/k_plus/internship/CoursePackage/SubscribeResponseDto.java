package com.k_plus.internship.CoursePackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class SubscribeResponseDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("course_id")
    private String courseId;
}
