package com.k_plus.internship.QuestionPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.k_plus.internship.OptionPackage.OptionRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class QuestionRequestDto {

    @JsonProperty(value = "question_text", required = true)
    private String questionText;

    @JsonProperty(value = "test_id", required = true)
    private UUID testId;

    @JsonProperty(value = "options", required = true)
    private List<OptionRequestDto> options;
}
