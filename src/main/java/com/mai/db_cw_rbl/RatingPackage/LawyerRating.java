package com.mai.db_cw_rbl.RatingPackage;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LawyerRating {

    private UUID id;

    private UUID lawyerId;

    private UUID questionId;

    private int rating;

    private String comment;

    private LocalDateTime createdAt;

}
