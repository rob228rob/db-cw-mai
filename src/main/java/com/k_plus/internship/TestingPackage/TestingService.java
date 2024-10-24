package com.k_plus.internship.TestingPackage;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.TestingNotFoundException;
import com.k_plus.internship.QuestionPackage.Question;

import lombok.RequiredArgsConstructor;

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

        //responseDto.setQuestionIds(mapQuestionsToListIds(testing.getQuestions()));

        return responseDto;
    }

    private List<UUID> mapQuestionsToListIds(Collection<Question> questions) {
        if (questions.isEmpty()) {
            return new ArrayList<>();
        }

        return questions.stream()
                .map(Question::getId)
                .sorted(Comparator.naturalOrder())
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

    public void saveTesting(Testing testing) {
        testingRepository.save(testing);
    }

    public List<Testing> findAllTestsByCourseId(UUID courseId) {
        return testingRepository.findAllTestsByCourseId(courseId);
    }

    public List<TestingResponseAdminDto> findAllTestsByCourseIdReturningAdminDto(UUID courseId) {
    List<Testing> testsList = testingRepository.findAllTestsByCourseId(courseId);

    return testsList
            .stream()
            .map(this::mapTestsToAdminDto)
            .toList();
    }

    private TestingResponseAdminDto mapTestsToAdminDto(Testing test) {
    TestingResponseAdminDto responseDto = modelMapper.map(test, TestingResponseAdminDto.class);

    return responseDto;
    }

}
