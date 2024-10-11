package com.k_plus.internship.UserAnswerPackage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.k_plus.internship.OptionPackage.OptionResponseDto;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswerResponseDto {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("test_id")
    private UUID testId;

    @JsonProperty("options")
    private List<QuestionAnsResponseDto> options;
}
