package com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedAnswerResponse {

    @JsonProperty("answer_id")
    private UUID answerId;

    @JsonProperty("question_id")
    private UUID questionId;

    @JsonProperty("lawyer_id")
    private UUID lawyerId;
}
