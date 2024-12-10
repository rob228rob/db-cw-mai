package com.mai.db_cw_rbl.confirmed_answers;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.confirmed_answers.dao.ConfirmedAnsDao;
import com.mai.db_cw_rbl.confirmed_answers.dto.ConfirmedAnswerRequest;
import com.mai.db_cw_rbl.confirmed_answers.dto.ConfirmedAnswerResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmedAnswerService {

    private final ConfirmedAnsDao confirmedAnsDao;

    private final ModelMapper modelMapper;

    public Optional<ConfirmedAnswer> getConfirmedAnswerByQuestionId(UUID questionId) {
        return confirmedAnsDao.findByQuestionId(questionId);
    }

    public boolean createConfirmedAnswer(ConfirmedAnswer confirmedAnswer) {
        return confirmedAnsDao.save(confirmedAnswer);
    }

    public boolean removeConfirmedAnswer(UUID questionId) {
        return confirmedAnsDao.deleteByQuestionId(questionId);
    }

    @Transactional
    public ConfirmedAnswerResponse confirm(ConfirmedAnswerRequest confirmedAnswerRequest) {
        UUID questionId = confirmedAnswerRequest.questionId();
        ConfirmedAnswer ans = ConfirmedAnswer.builder()
                .id(Generators.timeBasedEpochGenerator().generate())
                .answerId(confirmedAnswerRequest.answerId())
                .questionId(questionId)
                .build();
        confirmedAnsDao.save(ans);

        return modelMapper.map(ans, ConfirmedAnswerResponse.class);
    }

    public boolean checkConfirmationByAnswerId(UUID answerId) {
        return confirmedAnsDao.checkConfirmation(answerId);
    }
}
