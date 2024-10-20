package com.k_plus.internship.QuestionPackage;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.QuestionNotFoundException;
import com.k_plus.internship.OptionPackage.Option;
import com.k_plus.internship.OptionPackage.OptionResponseDto;
import com.k_plus.internship.OptionPackage.OptionService;
import com.k_plus.internship.TestingPackage.TestingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final TestingService testingService;

    private final ModelMapper modelMapper;

    private final OptionService optionService;

    //TODO: Refactor and simplify method!
    @Transactional()
    public QuestionResponseDto addQuestion(QuestionRequestDto questionDto) {
        //var question1 = modelMapper.map(questionDto, Question.class);
        var question1 = new Question();
        question1.setId(Generators.timeBasedEpochGenerator().generate());
        questionRepository.save(question1);
        var question = findQuestionById(question1.getId());
        var test = testingService.findTestingById(questionDto.getTestId());
        question.setTesting(test);
        question.setQuestionText(questionDto.getQuestionText());

        List<Option> options = questionDto.getOptions().stream().map(optionDto -> {
            Option option1 = new Option();
            option1.setId(Generators.timeBasedEpochGenerator().generate());
            optionService.saveOption(option1);
            var option = optionService.findOptionById(option1.getId());
            option.setOptionText(optionDto.getOptionText());
            option.setCorrect(optionDto.isCorrect());
            option.setQuestion(question);
            log.info("\n Option: \n" + option.getId());
            optionService.saveOption(option);
            return option;
        }).collect(Collectors.toList());

        question.setOptions(options);
        Question savedQuestion = questionRepository.save(question);
        log.debug("\n\n\nSAVED: {}!!!\n\n\n", savedQuestion.getId());
        test.setQuestionCount(test.getQuestionCount().equals(0) ? 1 : test.getQuestionCount() + 1);
        testingService.saveTesting(test);
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

    public Question findQuestionById(UUID uuid) {
        return questionRepository.findById(uuid).orElseThrow(
                () -> new QuestionNotFoundException("Question with uuid: " + uuid + " not found"));
    }

    public QuestionResponseDto findQuestionByIdReturningDto(UUID uuid) {
        var question = findQuestionById(uuid);
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

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public List<QuestionResponseDto> findAllQuestionsByTestingId(UUID testingId) {
    List<Question> questionsList = questionRepository.findAllQuestionsByTestingId(testingId);

    return questionsList
            .stream()
            .map(this::mapQuestionsToResponseDto)
            .toList();
    }
    
    private QuestionResponseDto mapQuestionsToResponseDto(Question question) {
        QuestionResponseDto responseDto = modelMapper.map(question, QuestionResponseDto.class);
    
        return responseDto;
        }
    
    @Transactional
    public void deleteQuestion(UUID uuid) {
        questionRepository.deleteById(uuid);
    }
}
