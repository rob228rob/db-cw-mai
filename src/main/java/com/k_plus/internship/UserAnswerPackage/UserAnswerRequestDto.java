package com.k_plus.internship.UserAnswerPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserAnswerRequestDto {

    @JsonProperty(value = "correct", required = true)
    private boolean correct;

    @JsonProperty(value = "user_id", required = true)
    private UUID userId;

    @JsonProperty(value = "question_id", required = true)
    private UUID questionId;

    @JsonProperty(value = "option_id", required = true)
    private UUID optionId;

    @JsonProperty(value = "test_id", required = true)
    private UUID testId;
}
