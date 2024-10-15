package com.k_plus.internship.StatisticsPackage;

import com.k_plus.internship.CommonPackage.CustomExceptions.TestingNotFoundException;
import com.k_plus.internship.RatingPackage.UserRatingService;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.TestingPackage.TestingService;
import com.k_plus.internship.UserAnswerPackage.UserAnswer;
import com.k_plus.internship.UserAnswerPackage.UserAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {

    private final TestingService testingService;

    private final UserAnswerService userAnswerService;

    private final UserRatingService userRatingService;

    public StatResponseDto findUserRankByCurse(UUID courseId, UUID userId) {
        log.info("\n\n\n\n---------------------------------");
        List<Testing> testingList = testingService.findAllTestsByCourseId(courseId);
        log.info("testingList size: {}", testingList.size());
        if (testingList.isEmpty()) {
            throw new TestingNotFoundException("tests by course id not found: " + courseId);
        }

        var ansList = testingList.stream()
                .map(x -> {
                    var userAnswers = userAnswerService.findAllUserAnswersByTestId(userId, x.getId());
                    long totalAnswersCount = userAnswers.size();
                    long correctAnswersCount = userAnswers.stream()
                            .filter(UserAnswer::isCorrect)
                            .count();
                    log.info("\ntotal ans count: {}\n", totalAnswersCount);
                    return List.of(correctAnswersCount, totalAnswersCount);
                })
                .reduce(List.of(0L, 0L),
                        (list1, list2) -> List.of(
                                list1.get(0) + list2.get(0),
                                list1.get(1) + list2.get(1)));

        var userRank = userRatingService.calcUserRatingByCourse(userId, courseId);
        log.info("Total answers COUNT: {}", ansList.get(1));
        log.info("Total correct answers COUNT: {}", ansList.get(0));
        log.info("Rank user: {}", userRank);
        log.info("----------------------------------\n\n\n\n");
        return StatResponseDto.builder()
                .rank(userRank)
                .percentageCorrect(((double) ansList.get(0) /ansList.get(1)) * 100)
                .userId(userId)
                .courseId(courseId)
                .build();
    }

}
