package com.mai.db_cw_rbl.lawyer_answers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerCreationRequest {

    @JsonProperty("lawyer_id")
    private UUID lawyerId;

    @JsonProperty("question_id")
    private UUID questionId;

    @JsonProperty("answer_text")
    private String answer;
}
