package com.mai.db_cw_rbl.LawyerAnswersPackage.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponse {

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("lawyer_id")
    private String lawyerId;

    @JsonProperty("creation_date")
    private LocalDateTime createdAt;

    @JsonProperty("lawyer_data")
    private LawyerData lawyerData;

    @Builder
    public record LawyerData(
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName) {}
}
