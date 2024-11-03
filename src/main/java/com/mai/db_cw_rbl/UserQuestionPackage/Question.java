package com.mai.db_cw_rbl.UserQuestionPackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    private UUID id;

    private UUID userId;

    private String questionTitle;

    private String questionText;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
