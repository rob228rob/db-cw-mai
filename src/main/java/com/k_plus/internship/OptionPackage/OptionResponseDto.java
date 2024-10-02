package com.k_plus.internship.OptionPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.k_plus.internship.QuestionPackage.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OptionResponseDto {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "correct")
    private boolean correct;

    @JsonProperty(value = "option_text")
    private String optionText;

    @JsonProperty(value = "question_id")
    UUID questionId;
}
