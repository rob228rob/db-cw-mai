package com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public record ConfirmedAnswerRequest(
        @JsonProperty("question_id") UUID questionId,
        @JsonProperty("answer_id") UUID answerId
) {
}
