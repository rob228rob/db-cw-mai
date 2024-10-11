package com.k_plus.internship.UserAnswerPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.OptionPackage.OptionService;
import com.k_plus.internship.QuestionPackage.QuestionService;
import com.k_plus.internship.TestingPackage.TestingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    private final TestingService testingService;

    private final QuestionService questionService;

    private final OptionService optionService;

    private final ModelMapper modelMapper;

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

    public UserAnswerResponseDto submitTest(UserAnswerRequestDto userAnswerRequestDto) {
        //Don't forget save to DB

        var optionResponseDtoList = userAnswerRequestDto.getUserAnswers()
                .stream()
                .map(x -> QuestionAnsResponseDto.builder()
                        .optionId(x.getOptionId())
                        .questionId(x.getQuestionId())
                        .correct(optionService.optionIsCorrect(x.getOptionId()))
                        .build())
                .peek(x -> {
                    UserAnswer ua = new UserAnswer();
                    ua.setId(Generators.timeBasedEpochGenerator().generate());
                    ua.setCorrect(x.isCorrect());
                    ua.setTesting(testingService.findTestingById(userAnswerRequestDto.getTestId()));
                    ua.setQuestion(questionService.findQuestionById(x.getQuestionId()));
                    ua.setOption(optionService.findOptionById(x.getOptionId()));
                    userAnswerRepository.save(ua);
                })
                .toList();

        return UserAnswerResponseDto.builder()
                .testId(userAnswerRequestDto.getTestId())
                .userId(userAnswerRequestDto.getUserId())
                .options(optionResponseDtoList)
                .build();
    }
}
