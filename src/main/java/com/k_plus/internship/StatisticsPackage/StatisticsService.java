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

    public StatResponseDto findStatsRankByCourse(UUID courseId, UUID userId) {
        List<Testing> testingList = testingService.findAllTestsByCourseId(courseId);
        log.info("testingList size: {}", testingList.size());
        if (testingList.isEmpty()) {
            throw new TestingNotFoundException("SERVICE STATISTICS: tests by course id not found: " + courseId);
        }
        var questionsCount = testingList.stream().map(Testing::getQuestionCount).reduce(0, Integer::sum);

        var ansList = testingList.stream()
                .map(x -> {
                    var userAnswers = userAnswerService.findAllUserAnswersByTestId(userId, x.getId());
                    long totalAnswersCount = userAnswers.size();
                    long correctAnswersCount = userAnswers.stream()
                            .filter(UserAnswer::isCorrect)
                            .count();
                    return List.of(correctAnswersCount, totalAnswersCount);
                })
                .reduce(List.of(0L, 0L),
                        (list1, list2) -> List.of(
                                list1.get(0) + list2.get(0),
                                list1.get(1) + list2.get(1)));

        var totalQuestionsCount = Math.max(questionsCount, ansList.get(1));
        var userRank = userRatingService.calcUserRatingByCourse(userId, courseId);
        var percentageCorrect = ((double) ansList.get(0) / totalQuestionsCount) * 100;

        return StatResponseDto.builder()
                .rank(userRank)
                .percentageCorrect(percentageCorrect)
                .userId(userId)
                .courseId(courseId)
                .build();
    }

    //TODO: CHANGE LOGIC!!!!!!!!!!!   CERTIFICATE MUST BE SEND ONLY ONCE!!
//    private void generateAndSendPDFCertificate(UUID userId, UUID courseId) throws DocumentException, MessagingException, IOException {
//        var user = userService.findUserById(userId);
//        var course = courseService.findCourseById(courseId);
//        var userFullName = user.getFirstName() + " " + user.getLastName();
//        var subject = "Поздравляем с успешным прохождением курса!";
//        var pdfCertificate = certificateSenderService.generateCertificate(userFullName, course.getName());
//
//        emailSenderService.sendCertificate(
//                user.getEmail(),
//                subject,
//                userFullName,
//                course.getName(),
//                pdfCertificate);
//    }

}
