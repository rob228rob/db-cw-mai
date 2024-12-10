package com.mai.db_cw_rbl.user_questions;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.lawyer_answers.AnswerService;
import com.mai.db_cw_rbl.user.UserService;
import com.mai.db_cw_rbl.user_questions.dao.QuestionDao;
import com.mai.db_cw_rbl.user_questions.dto.QuestionCreationRequest;
import com.mai.db_cw_rbl.user_questions.dto.QuestionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDao questionDao;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final AnswerService answerService;

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
                    response.setCreatedAt(question.getCreatedAt().toLocalDate());
                    return response;
                })
                .toList();
    }

    public List<QuestionResponse> findAllQuestions() {
        return questionDao.findAllQuestions()
                .stream()
                .map(question -> modelMapper.map(question, QuestionResponse.class))
                .toList();
    }

    @Transactional
    public void deleteQuestionById(UUID questionId, String email) {
        var userId = userService.findUserByEmail(email).getId();

        boolean deletedQuestion = questionDao.deleteQuestion(questionId, userId);
        boolean deletedAnswers = answerService.deleteAllByQuestionId(questionId);
    }

    public void updateAnswered(boolean answered, UUID questionId) {
        questionDao.updateAnswered(answered, questionId);
    }
}
