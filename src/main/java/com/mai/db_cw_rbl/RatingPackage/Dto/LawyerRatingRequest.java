package com.mai.db_cw_rbl.RatingPackage.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LawyerRatingRequest(
        @NotNull @JsonProperty("lawyer_id") UUID lawyerId,
        @NotNull @JsonProperty("question_id") UUID questionId,
        @JsonProperty("rating") int rating,
        @NotNull @JsonProperty("comment") String comment
) {

}
