package com.k_plus.internship.CoursePackage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        return ResponseEntity.ok(courseService.findCourseById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CourseCreatedResponseDto> createCourse(@RequestBody CourseRequestDto courseRequestDto) {
        return ResponseEntity.ok(courseService.createCourse(courseRequestDto));
    }
}
