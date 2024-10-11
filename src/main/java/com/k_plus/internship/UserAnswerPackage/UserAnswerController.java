package com.k_plus.internship.UserAnswerPackage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-ans")
@RequiredArgsConstructor
public class UserAnswerController {
    private final UserAnswerService userAnswerService;

    @PostMapping("/submit-test")
    public ResponseEntity<UserAnswerResponseDto> submitTest(
            @RequestBody UserAnswerRequestDto userAnswerRequestDto) {
        var dto = userAnswerService.submitTest(userAnswerRequestDto);
        return ResponseEntity.ok().body(dto);
    }
}
