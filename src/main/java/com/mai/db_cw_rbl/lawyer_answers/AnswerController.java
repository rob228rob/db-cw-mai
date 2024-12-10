package com.mai.db_cw_rbl.lawyer_answers;

import com.mai.db_cw_rbl.lawyer_answers.dto.AnswerCreationRequest;
import com.mai.db_cw_rbl.lawyer_answers.dto.AnswerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
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
