package com.mai.db_cw_rbl.ConfirmedAnswersPackage;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dao.ConfirmedAnsDao;
import com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dto.ConfirmedAnswerRequest;
import com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dto.ConfirmedAnswerResponse;
import com.mai.db_cw_rbl.UserQuestionPackage.QuestionService;
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
    private final QuestionService questionService;
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
        questionService.updateAnswered(true, questionId);

        return modelMapper.map(ans, ConfirmedAnswerResponse.class);
    }

    public boolean checkConfirmationByAnswerId(UUID answerId) {
        return confirmedAnsDao.checkConfirmation(answerId);
    }
}
