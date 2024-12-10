package com.mai.db_cw_rbl.confirmed_answers;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ConfirmedAnswer {

    private UUID id;

    private UUID questionId;

    private UUID answerId;

    private LocalDateTime createdAt;
}
