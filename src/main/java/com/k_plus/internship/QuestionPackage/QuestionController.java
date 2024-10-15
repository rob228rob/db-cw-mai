package com.k_plus.internship.QuestionPackage;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/get/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable UUID id) {
        return ResponseEntity
                .ok(questionService.findQuestionByIdReturningDto(id));
    }

    @PostMapping("/add")
    public ResponseEntity<QuestionResponseDto> addQuestion(
            @RequestBody QuestionRequestDto questionRequestDto) {
        return ResponseEntity
                .ok(questionService.addQuestion(questionRequestDto));
    }

    @GetMapping("/get-all/{uuid}")
    public ResponseEntity<List<QuestionResponseDto>> findAllQuestionsByTestingId(@PathVariable UUID uuid) {
        return ResponseEntity
            .ok(questionService.findAllQuestionsByTestingId(uuid));
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<QuestionResponseDto> deleteQuestion(@PathVariable UUID uuid) {
        questionService.deleteQuestion(uuid);
        return ResponseEntity.noContent().build();
    }
}
