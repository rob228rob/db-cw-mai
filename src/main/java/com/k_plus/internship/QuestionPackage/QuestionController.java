package com.k_plus.internship.QuestionPackage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/get/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable UUID id) {
        return ResponseEntity
                .ok(questionService.getQuestionByIdReturningDto(id));
    }

    @PostMapping("/add")
    public ResponseEntity<QuestionResponseDto> addQuestion(
            @RequestBody QuestionRequestDto questionRequestDto) {
        return ResponseEntity
                .ok(questionService.addQuestion(questionRequestDto));
    }
}
