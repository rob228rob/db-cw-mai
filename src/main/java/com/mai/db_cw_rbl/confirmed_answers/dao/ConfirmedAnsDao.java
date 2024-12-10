package com.mai.db_cw_rbl.confirmed_answers.dao;

import com.mai.db_cw_rbl.confirmed_answers.ConfirmedAnswer;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmedAnsDao {

    Optional<ConfirmedAnswer> findByQuestionId(UUID questionId);

    boolean save(ConfirmedAnswer confirmedAnswer);

    boolean deleteByQuestionId(UUID questionId);

    boolean checkConfirmation(UUID answerId);
}
