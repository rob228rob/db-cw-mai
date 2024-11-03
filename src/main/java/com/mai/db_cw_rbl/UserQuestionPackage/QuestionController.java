package com.mai.db_cw_rbl.UserQuestionPackage;

import com.mai.db_cw_rbl.UserQuestionPackage.Dto.QuestionCreationRequest;
import com.mai.db_cw_rbl.UserQuestionPackage.Dto.QuestionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Validated
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{uuid}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(questionService.findQuestionById(uuid));
    }

    @GetMapping("/get-all-by")
    public ResponseEntity<List<QuestionResponse>> getQuestionByUserId(@RequestParam("user_id") UUID userId) {
        return ResponseEntity.ok().body(questionService.findQuestionByUserId(userId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<QuestionResponse>> getAllUserQuestions() {
        return ResponseEntity.ok().body(questionService.findAllQuestions());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> saveQuestion(
            @Valid @RequestBody QuestionCreationRequest creationRequest
    ) {
        return ResponseEntity.ok(questionService.saveQuestion(creationRequest));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<QuestionResponse> deleteQuestion(
            @RequestParam("question_id") UUID questionId,
            @RequestParam("user_id") UUID userId
    ) {
        questionService.deleteQuestionById(questionId, userId);

        return ResponseEntity.noContent().build();
    }
}
