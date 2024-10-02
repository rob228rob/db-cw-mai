package com.k_plus.internship.QuestionPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.QuestionNotFoundException;
import com.k_plus.internship.OptionPackage.Option;
import com.k_plus.internship.OptionPackage.OptionResponseDto;
import com.k_plus.internship.OptionPackage.OptionService;
import com.k_plus.internship.TestingPackage.TestingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final TestingService testingService;

    private final ModelMapper modelMapper;

    private final OptionService optionService;

    //TODO: Refactor and simplify method!
    @Transactional
    public QuestionResponseDto addQuestion(QuestionRequestDto questionDto) {
        var question = modelMapper.map(questionDto, Question.class);
        question.setId(Generators.timeBasedEpochGenerator().generate());

        var test = testingService.findTestingById(questionDto.getTestId());
        question.setTesting(test);

        List<Option> options = questionDto.getOptions().stream().map(optionDto -> {
            Option option = new Option();
            option.setOptionText(optionDto.getOptionText());
            option.setCorrect(optionDto.isCorrect());
            option.setQuestion(question);
            return option;
        }).toList();

        question.setOptions(options);

        Question savedQuestion = questionRepository.save(question);

        optionService.saveAllOptions(options);

        var responseDto = modelMapper.map(savedQuestion, QuestionResponseDto.class);
        responseDto.setTestId(questionDto.getTestId());
        responseDto.setOptions(questionDto.getOptions()
                .stream()
                .map(x -> {
                    var dto = modelMapper.map(x, OptionResponseDto.class);
                    dto.setQuestionId(question.getId());
                    return dto;
                })
                .toList()
        );

        return responseDto;
    }

    public Question getQuestionById(UUID uuid) {
        return questionRepository.findById(uuid).orElseThrow(
                () -> new QuestionNotFoundException("Question with uuid: " + uuid + " not found"));
    }

    public QuestionResponseDto getQuestionByIdReturningDto(UUID uuid) {
        var question = getQuestionById(uuid);
        QuestionResponseDto questionDto = modelMapper.map(question, QuestionResponseDto.class);
        questionDto.setTestId(question.getTesting().getId());
        questionDto.setOptions(
                question.getOptions()
                        .stream()
                        .map(optionService::mapToOptionResponseDto)
                        .toList()
        );

        return questionDto;
    }
}
