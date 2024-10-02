package com.k_plus.internship.OptionPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OptionRequestDto {

    @JsonProperty(value = "correct", required = true)
    private boolean correct;

    @JsonProperty(value = "option_text", required = true)
    private String optionText;

    @JsonProperty(value = "question_id", required = true)
    private UUID questionId;
}
