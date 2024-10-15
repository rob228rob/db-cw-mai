package com.k_plus.internship.CoursePackage;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/get-all")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        return ResponseEntity
                .ok(courseService.findAllCourses());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.findCourseByIdReturningDto(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CourseCreatedResponseDto> createCourse(@RequestBody CourseRequestDto courseRequestDto) {
        return ResponseEntity.ok(courseService.createCourse(courseRequestDto));
    }

    @GetMapping("/get-all/by-stud-id/{id}")
    public ResponseEntity<List<CourseResponseDto>> getAllCoursesByStudentId(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.findAllCoursesByStudentId(id));
    }

    // @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping("/get-all-id")
    public ResponseEntity<List<CourseResponseAdminDto>> getAllCoursesWithId() {
        return ResponseEntity
                .ok(courseService.findAllCoursesWithId());
    }

    @GetMapping("/is-user-enrolled")
    public ResponseEntity<Boolean> isUserEnrolledOnCourse(@RequestParam UUID userId, @RequestParam UUID courseId) {
        return ResponseEntity.ok(courseService.isUserEnrolledOnCourse(userId, courseId));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<SubscribeResponseDto> enrollInCourse(
            @RequestBody SubscribeRequestDto requestDto) {
        //TODO: additional logic
        courseService.enrollUserInCourse(requestDto.getCourseId(), requestDto.getUserId());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    @PostMapping("/update/{id}")
    public ResponseEntity<CourseResponseAdminDto> updateCourse(@RequestBody CourseResponseAdminDto courseRequestDto, @PathVariable UUID id) {
        return ResponseEntity.ok(courseService.updateCourse(courseRequestDto, id));
    }

    @GetMapping("/get-all-by-pattern")
    public ResponseEntity<List<CourseResponseDto>> getAllCoursesByPattern(@RequestParam String pattern) {
        return ResponseEntity.ok(courseService.findAllCoursesByPattern(pattern));
    }
}
