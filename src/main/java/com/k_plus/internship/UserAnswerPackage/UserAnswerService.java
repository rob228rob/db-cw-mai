package com.k_plus.internship.UserAnswerPackage;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    private final ModelMapper modelMapper;

    public UserAnswerResponseDto saveUserAnswer(UserAnswerRequestDto answerDto) {
        var userAnswer = modelMapper.map(answerDto, UserAnswer.class);
        UserAnswer userAnsw = userAnswerRepository.save(userAnswer);

        return modelMapper.map(userAnsw, UserAnswerResponseDto.class);
    }
}
