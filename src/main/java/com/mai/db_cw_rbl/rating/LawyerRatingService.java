package com.mai.db_cw_rbl.rating;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.rating.dao.LawyerRatingDao;
import com.mai.db_cw_rbl.rating.dto.LawyerRatingRequest;
import com.mai.db_cw_rbl.rating.dto.LawyerRatingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LawyerRatingService {

    private final LawyerRatingDao lawyerRatingDao;


    public void saveLawyerRatingByAnswerId(@Valid LawyerRatingRequest ratingRequest) {
        LawyerRating rating = LawyerRating.builder()
                .id(Generators.timeBasedEpochGenerator().generate())
                .lawyerId(ratingRequest.lawyerId())
                .questionId(ratingRequest.questionId())
                .comment(ratingRequest.comment())
                .rating(ratingRequest.rating())
                .createdAt(LocalDateTime.now())
                .build();

        lawyerRatingDao.saveRating(rating);
    }
//
//    public LawyerRating findLawyerRatingByAnswerId(UUID answerId) {
//        lawyerRatingDao.findRatingBAnswerId(answerId);
//    }

    public LawyerRatingResponse calculateAverageRating(UUID lawyerId) {
        var allLawyerRatings = lawyerRatingDao.findAllRatingsByLawyerId(lawyerId);
        if (allLawyerRatings.isEmpty()) {
            return LawyerRatingResponse.builder().build();
        }

        var answersCount = allLawyerRatings.size();

        var totalRating = allLawyerRatings.stream()
                .reduce(0.0, Double::sum);

        return LawyerRatingResponse.builder()
                .answerCount(answersCount)
                .lawyerId(lawyerId)
                .rating(totalRating / answersCount)
                .build();
    }

    public String findCommentByQuestionId(UUID questionId) {
        return lawyerRatingDao.findCommentByQuestionId(questionId);
    }
}
