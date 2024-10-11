package com.k_plus.internship.UserAnswerPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OptionAnswerRequestDto {

    @JsonProperty("option_id")
    private UUID optionId;

    @JsonProperty("question_id")
    private UUID questionId;
}
