package com.k_plus.internship.UserAnswerPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionAnsResponseDto {
    @JsonProperty(value = "option_id")
    private UUID optionId;

    @JsonProperty(value = "option_text")
    private String optionText;

    @JsonProperty(value = "correct")
    private boolean correct;

    @JsonProperty(value = "question_id")
    private UUID questionId;
}
