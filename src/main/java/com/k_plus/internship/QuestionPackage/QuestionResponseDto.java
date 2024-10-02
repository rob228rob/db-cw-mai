package com.k_plus.internship.QuestionPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.k_plus.internship.OptionPackage.Option;
import com.k_plus.internship.OptionPackage.OptionResponseDto;
import com.k_plus.internship.TestingPackage.Testing;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class QuestionResponseDto {

    private UUID id;

    @JsonProperty("question_text")
    private String questionText;

    @JsonProperty("test_id")
    private UUID testId;

    @JsonProperty("options")
    private List<OptionResponseDto> options;
}
