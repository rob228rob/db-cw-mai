package com.k_plus.internship.UserAnswerPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.EmailSender.EmailSenderService;
import com.k_plus.internship.OptionPackage.OptionService;
import com.k_plus.internship.QuestionPackage.QuestionService;
import com.k_plus.internship.RatingPackage.UserRatingRepository;
import com.k_plus.internship.RatingPackage.UserRatingService;
import com.k_plus.internship.StatisticsPackage.StatisticsService;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.TestingPackage.TestingService;
import com.k_plus.internship.UserPackage.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    private final TestingService testingService;

    private final QuestionService questionService;

    private final OptionService optionService;

    private final ModelMapper modelMapper;

    private final UserRatingService userRatingService;

    private final UserService userService;

    private final UserRatingRepository userRatingRepository;
    private final EmailSenderService emailSenderService;

    public UserAnswerResponseDto saveUserAnswer(UserAnswerRequestDto answerDto) {
        var userAnswer = modelMapper.map(answerDto, UserAnswer.class);
        userAnswer.setId(Generators.timeBasedEpochGenerator().generate());
        UserAnswer userAns = userAnswerRepository.save(userAnswer);

        var dtoAns = new UserAnswerResponseDto();

        return modelMapper.map(userAns, UserAnswerResponseDto.class);
    }

    private void saveUserAnswer(UserAnswer userAnswer) {
        userAnswer.setId(Generators.timeBasedEpochGenerator().generate());
        userAnswerRepository.save(userAnswer);
    }

    @Transactional
    public UserAnswerResponseDto submitTest(UserAnswerRequestDto userAnswerRequestDto) {
        var userId = userAnswerRequestDto.getUserId();
        var testId = userAnswerRequestDto.getTestId();
        //User have only 1 attempt
        var findCorrectCount = userAnswerRepository.findAllByUserIdAndByTestIdOrderById(userId, testId)
                .stream()
                .filter(UserAnswer::isCorrect)
                .count();
        var testById = testingService.findTestingById(testId);

        userAnswerRepository.deleteAllByUserIdAndTestingId(userId, testId);

        var optionResponseDtoList = userAnswerRequestDto.getUserAnswers().stream()
                .map(this::buildQuestionAnswerResponseDto)
                .peek(questionAnswerResponseDto ->
                        saveUserAnswer(userAnswerRequestDto, questionAnswerResponseDto))
                .toList();

        // rating service (already done)

        userRatingService.updateUserRating(
                userId,
                testById.getCourse().getId(),
                testId,
                (int) optionResponseDtoList.stream()
                        .filter(QuestionAnsResponseDto::isCorrect).count());

        return UserAnswerResponseDto.builder()
                .testId(userAnswerRequestDto.getTestId())
                .userId(userAnswerRequestDto.getUserId())
                .options(optionResponseDtoList)
                .build();
    }

    private void deleteAllByUserIdAndTestingId(UUID userId, UUID testId, long findCorrectCount) {

    }

    private QuestionAnsResponseDto buildQuestionAnswerResponseDto(OptionAnswerRequestDto x) {
        return QuestionAnsResponseDto.builder()
                .optionId(x.getOptionId())
                .questionId(x.getQuestionId())
                .correct(optionService.optionIsCorrect(x.getOptionId()))
                .build();
    }

    private void saveUserAnswer(UserAnswerRequestDto requestDto, QuestionAnsResponseDto responseDto) {
        UserAnswer ua = new UserAnswer();
        ua.setId(Generators.timeBasedEpochGenerator().generate());
        ua.setUser(userService.findUserById(requestDto.getUserId()));
        ua.setCorrect(responseDto.isCorrect());
        ua.setTesting(testingService.findTestingById(requestDto.getTestId()));
        ua.setQuestion(questionService.findQuestionById(responseDto.getQuestionId()));
        ua.setOption(optionService.findOptionById(responseDto.getOptionId()));
        userAnswerRepository.save(ua);
    }

    public List<UserAnswer> findAllUserAnswersByTestId(UUID userId, UUID testId) {

        return userAnswerRepository.findAllByUserIdAndByTestIdOrderById(userId, testId);
    }
}
