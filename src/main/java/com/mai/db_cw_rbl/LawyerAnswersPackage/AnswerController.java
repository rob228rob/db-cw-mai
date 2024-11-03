package com.mai.db_cw_rbl.LawyerAnswersPackage;

import com.mai.db_cw_rbl.LawyerAnswersPackage.Dto.AnswerCreationRequest;
import com.mai.db_cw_rbl.LawyerAnswersPackage.Dto.AnswerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
@PreAuthorize("hasAnyRole('ADMIN', 'LAWYER')")
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponse> getAnswerById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                answerService.findAnswerById(id));
    }

    @GetMapping("/get-all/{id}")
    public ResponseEntity<List<AnswerResponse>> getAllAnswersByQuestionId(@PathVariable UUID id) {
        return ResponseEntity.ok(answerService.findAllAnswersByQuestionIdWithLawyerData(id));
    }

    @GetMapping("/get-all-by")
    public ResponseEntity<List<AnswerResponse>> getAnswerByLawyerId(@RequestParam("lawyer_id") UUID id) {
        return ResponseEntity.ok(
                answerService.findAllAnswersByLawyerId(id));
    }

    @PostMapping("/create")
    public ResponseEntity<AnswerResponse> createAnswer(
            @Valid @RequestBody AnswerCreationRequest creationRequest
    ) {
        return ResponseEntity.ok(
                answerService.saveAnswer(creationRequest));
    }
}
