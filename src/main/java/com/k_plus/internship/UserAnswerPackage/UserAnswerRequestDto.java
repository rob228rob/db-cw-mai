package com.k_plus.internship.UserAnswerPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserAnswerRequestDto {

    @JsonProperty(value = "user_id", required = true)
    private UUID userId;

    @JsonProperty(value = "test_id", required = true)
    private UUID testId;

    @JsonProperty(value = "course_id", required = true)
    private UUID courseId;

    @JsonProperty(value = "user_answers", required = true)
    private List<OptionAnswerRequestDto> userAnswers;
}
