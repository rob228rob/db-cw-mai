package com.mai.db_cw_rbl.LawyerAnswersPackage.Dao;

import com.mai.db_cw_rbl.LawyerAnswersPackage.Answer;
import com.mai.db_cw_rbl.LawyerAnswersPackage.Dto.AnswerResponse;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnswerDao {

    Optional<Answer> findById(UUID id);

    List<Answer> findAllByLawyerId(UUID lawyerId);

    boolean save(Answer answer);

    boolean delete(Answer answer);

    boolean update(Answer answer);

    List<Answer> findAllByQuestionId(UUID uuid);

    List<AnswerResponse> findAllByQuestionIdWithUserData(UUID questionId);
}
