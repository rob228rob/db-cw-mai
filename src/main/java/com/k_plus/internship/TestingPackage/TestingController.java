package com.k_plus.internship.TestingPackage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestingController {

    private final TestingService testingService;

    @GetMapping("/{id}")
    public ResponseEntity<TestingResponseDto> getTestingById(@PathVariable UUID id) {
        return ResponseEntity
                .ok()
                .body(testingService.findTestingByIdReturningDto(id));
    }

    @PreAuthorize("hasAnyRole(\"ADMIN\")")
    @PostMapping("/add")
    public ResponseEntity<TestingResponseDto> addTesting(
            @RequestBody @Valid TestingRequestDto testingRequestDto
    ) {
        return ResponseEntity
                .ok()
                .body(testingService.saveTesting(testingRequestDto));

    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TestingResponseDto> deleteTesting(@PathVariable UUID id) {
        testingService.deleteTestingById(id);

        return ResponseEntity.noContent().build();
    }
}
