package com.k_plus.internship.OptionPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.QuestionPackage.QuestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OptionService {

    private final OptionRepository optionRepository;

    private final ModelMapper modelMapper;

    //private final QuestionService questionService;

    public OptionResponseDto saveOption(OptionRequestDto optionDto) {
        var option = modelMapper.map(optionDto, Option.class);
        option.setId(Generators.timeBasedEpochGenerator().generate());
        //option.setQuestion(questionService.getQuestionById(optionDto.getQuestionId()));
        optionRepository.save(option);

        var responseDto = modelMapper.map(option, OptionResponseDto.class);
        responseDto.setQuestionId(optionDto.getQuestionId());

        return responseDto;
    }

    public OptionResponseDto mapToOptionResponseDto(Option option) {
        OptionResponseDto dto = modelMapper.map(option, OptionResponseDto.class);
        dto.setQuestionId(option.getQuestion().getId());

        return dto;
    }

    @Transactional
    public void saveAllOptions(List<Option> options) {
        optionRepository.saveAll(options);
    }
}
