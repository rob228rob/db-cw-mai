package com.mai.db_cw_rbl.user_questions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreationRequest {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("question_data")
    private QuestionData questionData;

    @Data
    public static class QuestionData {

        @JsonProperty("title")
        private String title;

        @JsonProperty("text")
        private String questionText;
    }
}
