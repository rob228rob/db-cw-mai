package com.mai.db_cw_rbl.RatingPackage.Dao;

import com.mai.db_cw_rbl.RatingPackage.Dto.AnswerRating;
import com.mai.db_cw_rbl.RatingPackage.LawyerRating;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LawyerRatingDao {

    Optional<LawyerRating> findRatingById(UUID uuid);

    boolean saveRating(LawyerRating lawyerRating);

    Optional<LawyerRating> findRatingBAnswerId(UUID answerId);

    //TODO: in service layer CALC AVG double RATING!
    List<Double> findAllRatingsByLawyerId(UUID lawyerId);

    String findCommentByQuestionId(UUID questionId);
}
