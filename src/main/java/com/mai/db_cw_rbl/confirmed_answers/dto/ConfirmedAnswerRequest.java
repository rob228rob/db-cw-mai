package com.mai.db_cw_rbl.confirmed_answers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ConfirmedAnswerRequest(
        @JsonProperty("question_id") UUID questionId,
        @JsonProperty("answer_id") UUID answerId
) {
}
