package com.mai.db_cw_rbl.user_questions.dao;

import com.mai.db_cw_rbl.user_questions.Question;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionDao {

    Optional<Question> findByQuestionId(UUID uuid);

    Optional<Question> findByTitle(String title);

    List<Question> findAllByUserId(UUID userId);

    boolean saveQuestion(Question question);

    boolean deleteQuestion(UUID questionid, UUID userId);

    boolean updateQuestion(Question question);

    List<Question> findAllQuestions();

    void updateAnswered(boolean answered, UUID id);
}
