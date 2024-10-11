package com.k_plus.internship.OptionPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.OptionNotFoundException;
import com.k_plus.internship.QuestionPackage.QuestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    public void saveOption(Option option) {
        optionRepository.save(option);
    }

    public Option findOptionById(UUID id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new OptionNotFoundException("option with id not found: " + id));
    }

    public boolean optionIsCorrect(UUID optionId) {
        return optionRepository.optionIsCorrect(optionId);
    }
}
