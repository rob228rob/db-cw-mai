package com.mai.db_cw_rbl.lawyer_answers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponse {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("confirmed")
    private boolean confirmed;

    @JsonProperty("rated")
    private boolean rated;

    @JsonProperty("lawyer_rating")
    private double lawyerRating;

    @JsonProperty("rating_comment")
    private String ratingComment;

    @JsonProperty("lawyer_id")
    private UUID lawyerId;

    @JsonProperty("creation_date")
    private LocalDateTime createdAt;

    @JsonProperty("lawyer_data")
    private LawyerData lawyerData;

    @Builder
    public record LawyerData(
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName) {}
}
