package com.k_plus.internship.TestingPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.TestingNotFoundException;
import com.k_plus.internship.QuestionPackage.Question;
import com.k_plus.internship.QuestionPackage.QuestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestingService {

    private final TestingRepository testingRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public TestingResponseDto saveTesting(TestingRequestDto testingDto) {
        Testing testing = modelMapper.map(testingDto, Testing.class);
        testing.setId(Generators.timeBasedGenerator().generate());
        var responseDto = modelMapper.map(testingRepository.save(testing), TestingResponseDto.class);

        responseDto.setQuestionIds(mapQuestionsToListIds(testing.getQuestions()));

        return responseDto;
    }

    private List<UUID> mapQuestionsToListIds(Collection<Question> questions) {
        if (questions.isEmpty()) {
            return new ArrayList<>();
        }

        return questions.stream()
                .map(Question::getId)
                .toList();
    }

    public Testing findTestingById(UUID uuid) {
        return testingRepository.findById(uuid).orElseThrow(
                () -> new TestingNotFoundException("Could not find testing with id " + uuid)
        );
    }

    public TestingResponseDto findTestingByIdReturningDto(UUID uuid) {
        var testing = findTestingById(uuid);
        var testingDto = modelMapper.map(testing, TestingResponseDto.class);
        testingDto.setQuestionIds(mapQuestionsToListIds(testing.getQuestions()));

        return testingDto;
    }

    public void deleteTestingById(UUID id) {
        testingRepository.deleteById(id);
    }
}
