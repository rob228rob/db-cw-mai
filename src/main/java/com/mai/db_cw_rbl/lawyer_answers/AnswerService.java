package com.mai.db_cw_rbl.lawyer_answers;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.confirmed_answers.ConfirmedAnswerService;
import com.mai.db_cw_rbl.infrastructure.exceptions.EntityNotFoundException;
import com.mai.db_cw_rbl.lawyer_answers.dao.AnswerDao;
import com.mai.db_cw_rbl.lawyer_answers.dto.AnswerCreationRequest;
import com.mai.db_cw_rbl.lawyer_answers.dto.AnswerResponse;
import com.mai.db_cw_rbl.rating.LawyerRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerDao answerDao;
    private final ModelMapper modelMapper;
    private final ConfirmedAnswerService confirmedAnswerService;
    private final LawyerRatingService lawyerRatingService;

    public AnswerResponse findAnswerById(UUID id) {
        var answer = answerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Answer with id: " + id + " not found"));

        return modelMapper.map(answer, AnswerResponse.class);
    }

    public AnswerResponse saveAnswer(@Valid AnswerCreationRequest creationRequest) {
        Answer ans = Answer.builder()
                .id(Generators.timeBasedEpochGenerator().generate())
                .lawyerId(creationRequest.getLawyerId())
                .questionId(creationRequest.getQuestionId())
                .answer(creationRequest.getAnswer())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        answerDao.save(ans);

        return modelMapper.map(ans, AnswerResponse.class);
    }

    private AnswerResponse mapAnswerToAnswerResponse(Answer ans) {
        AnswerResponse response = modelMapper.map(ans, AnswerResponse.class);
        response.setId(ans.getId());
        var ratingResp = lawyerRatingService.calculateAverageRating(ans.getLawyerId());
        var ratingComment = lawyerRatingService.findCommentByQuestionId(ans.getQuestionId());
        response.setRated(true);
        response.setLawyerRating(ratingResp.rating());
        response.setRatingComment(ratingComment);
        return response;
    }

    public List<AnswerResponse> findAllAnswersByLawyerId(UUID id) {

        return answerDao.findAllByLawyerId(id)
                .stream()
                .map(this::mapAnswerToAnswerResponse)
                .toList();
    }

    public List<AnswerResponse> findAllAnswersByQuestionId(UUID uuid) {

        return answerDao.findAllByQuestionId(uuid)
                .stream()
                .map(ans -> modelMapper.map(ans, AnswerResponse.class))
                .toList();
    }

    public List<AnswerResponse> findAllAnswersByQuestionIdWithLawyerData(UUID questionId) {
        return answerDao.findAllByQuestionIdWithUserData(questionId).stream()
                .peek(ans -> {
                    var ratingResp = lawyerRatingService.calculateAverageRating(ans.getLawyerId());
                    ans.setRated(ratingResp.rating() > 1.0);
                    ans.setLawyerRating(ratingResp.rating());
                    boolean confirmation = confirmedAnswerService.checkConfirmationByAnswerId(ans.getId());
                    ans.setConfirmed(confirmation);
                    var ratingComment = lawyerRatingService.findCommentByQuestionId(questionId);
                    ans.setRatingComment(ratingComment);
                })
                .toList();
    }

    public boolean deleteAllByQuestionId(UUID questionId) {
        return answerDao.deleteByQuestionId(questionId);
    }
}
