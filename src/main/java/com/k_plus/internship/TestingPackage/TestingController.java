package com.k_plus.internship.TestingPackage;

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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestingController {

    private final TestingService testingService;

    @GetMapping("/get/{id}")
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

    @GetMapping("/get-all/{uuid}")
    public ResponseEntity<List<TestingResponseAdminDto>> findAllTestsByCourseID(@PathVariable UUID uuid) {
        return ResponseEntity
            .ok(testingService.findAllTestsByCourseId(uuid));
    }
}
