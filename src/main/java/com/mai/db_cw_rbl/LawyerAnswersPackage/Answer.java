package com.mai.db_cw_rbl.LawyerAnswersPackage;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    private UUID id;

    private UUID lawyerId;

    private UUID questionId;

    private String answer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
