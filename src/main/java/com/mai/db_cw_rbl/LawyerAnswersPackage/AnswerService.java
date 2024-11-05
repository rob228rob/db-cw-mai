package com.mai.db_cw_rbl.LawyerAnswersPackage;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.ConfirmedAnswersPackage.ConfirmedAnswerService;
import com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions.EntityNotFoundException;
import com.mai.db_cw_rbl.LawyerAnswersPackage.Dao.AnswerDao;
import com.mai.db_cw_rbl.LawyerAnswersPackage.Dto.AnswerCreationRequest;
import com.mai.db_cw_rbl.LawyerAnswersPackage.Dto.AnswerResponse;
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

    public List<AnswerResponse> findAllAnswersByLawyerId(UUID id) {
        var allByLawyerId = answerDao.findAllByLawyerId(id)
                .stream()
                .map(ans -> {
                    AnswerResponse response = modelMapper.map(ans, AnswerResponse.class);
                    response.setId(ans.getId());
                    return response;
                })
                .toList();

        return allByLawyerId;
    }

    public List<AnswerResponse> findAllAnswersByQuestionId(UUID uuid) {
        var allByLawyerId = answerDao.findAllByQuestionId(uuid)
                .stream()
                .map(ans -> modelMapper.map(ans, AnswerResponse.class))
                .toList();

        return allByLawyerId;
    }

    public List<AnswerResponse> findAllAnswersByQuestionIdWithLawyerData(UUID uuid) {
        return answerDao.findAllByQuestionIdWithUserData(uuid).stream()
                .peek(ans -> {
                    boolean confirmation = confirmedAnswerService.checkConfirmationByAnswerId(ans.getId());
                    ans.setConfirmed(confirmation);
                })
                .toList();
    }
}
