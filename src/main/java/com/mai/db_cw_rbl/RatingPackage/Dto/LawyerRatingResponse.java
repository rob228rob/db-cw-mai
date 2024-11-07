package com.mai.db_cw_rbl.RatingPackage.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record LawyerRatingResponse(
       @JsonProperty("lawyer_id") UUID lawyerId,
       @JsonProperty("avg_rating") double rating,
       @JsonProperty("answer_count") int answerCount
) {
}
