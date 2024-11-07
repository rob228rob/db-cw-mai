package com.mai.db_cw_rbl.RatingPackage.Dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AnswerRating {
//    private UUID lawyerId;
    private double answerRating;
}

