package com.mai.db_cw_rbl.UserQuestionPackage;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.UserQuestionPackage.Dao.QuestionDao;
import com.mai.db_cw_rbl.UserQuestionPackage.Dto.QuestionCreationRequest;
import com.mai.db_cw_rbl.UserQuestionPackage.Dto.QuestionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDao questionDao;

    private final ModelMapper modelMapper;

    public QuestionResponse findQuestionById(UUID uuid) {
        var question = questionDao.findByQuestionId(uuid);

        return modelMapper.map(question, QuestionResponse.class);
    }

    public QuestionResponse saveQuestion(@Valid QuestionCreationRequest creationRequest) {
        Question question = modelMapper.map(creationRequest.getQuestionData(), Question.class);
        question.setId(Generators.timeBasedGenerator().generate());
        question.setUserId(creationRequest.getUserId());
        question.setCreatedAt(LocalDateTime.now());
        boolean b = questionDao.saveQuestion(question);
        return modelMapper.map(question, QuestionResponse.class);
    }

    public List<QuestionResponse> findQuestionByUserId(UUID userId) {
        var questions = questionDao.findAllByUserId(userId);

        return questions.stream()
                .map(question -> {
                    QuestionResponse response = modelMapper.map(question, QuestionResponse.class);
                    response.setCreationDate(question.getCreatedAt().toLocalDate());
                    return response;
                })
                .toList();
    }

    public List<QuestionResponse> findAllQuestions() {
        return questionDao.findAllQuestions()
                .stream()
                .map(question -> {
                    QuestionResponse response = modelMapper.map(question, QuestionResponse.class);
                    response.setAnswered(false);
                    return response;
                })
                .toList();
    }

    public void deleteQuestionById(UUID questionId, UUID userId) {
        questionDao.deleteQuestion(questionId, userId);
    }
}
