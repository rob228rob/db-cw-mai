package com.k_plus.internship.CoursePackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.k_plus.internship.UserPackage.User;
import com.k_plus.internship.UserPackage.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.ArticlePackage.Article;
import com.k_plus.internship.CommonPackage.CustomExceptions.CourseNotFoundException;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.TestingPackage.TestingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final ModelMapper modelMapper;

    private final TestingService testingService;

    private final CourseRepository courseRepository;
    private final UserService userService;

    @Transactional
    public CourseCreatedResponseDto createCourse(CourseRequestDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);
        course.setId(Generators.timeBasedEpochGenerator().generate());

//        course.setArticles(mapIdToArticles(courseDto.getArticleIds()));
//        course.setTestings(mapIdToTestings(courseDto.getTestIds()));

        var dto = modelMapper.map(courseRepository.save(course), CourseCreatedResponseDto.class);
//        dto.setArticleIds(courseDto.getArticleIds());
//        dto.setTestingIds(courseDto.getTestIds());

        return dto;
    }

    private Collection<Testing> mapIdToTestings(List<UUID> testIds) {
        if (testIds.isEmpty()) {
            return new ArrayList<>();
        }

        return testIds.stream()
                .map(testingService::findTestingById)
                .toList();
    }

//    private Collection<Article> mapIdToArticles(List<UUID> articleIds) {
//        if (articleIds.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return articleIds
//                .stream()
//                .map(articleService::findArticleById)
//                .toList();
//    }

    @Transactional
    public List<CourseResponseDto> findAllCourses() {
        var response = courseRepository.findAllCourses()
                .stream()
                .map(this::mapCourseToDto)
                .peek(x -> x.setPaid(false))
                .toList();

        if (response.isEmpty()) {
            return new ArrayList<>();
        }

        return response;
    }

    public Course findCourseById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("There's no course with id " + id));
    }

    public CourseResponseDto findCourseByIdReturningDto(UUID id) {
        return mapCourseToDto(courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with id " + id + " not found")));
    }

    private CourseResponseDto mapCourseToDto(Course course) {
        CourseResponseDto responseDto = modelMapper.map(course, CourseResponseDto.class);
        var articleIds = course.getArticles()
                .stream()
                .map(x -> ResponseCourseHeaderDto.builder()
                        .id(x.getId())
                        .header(x.getTitle())
                        .displayOrder(x.getDisplayOrder())
                        .build())
                .toList();

        var testIds = course.getTestings()
                .stream()
                .map(x -> ResponseCourseHeaderDto.builder()
                        .id(x.getId())
                        .header(x.getName())
                        .displayOrder(x.getDisplayOrder())
                        .build())
                .toList();

        responseDto.setArticleIds(articleIds);
        responseDto.setTestingIds(testIds);
        return responseDto;
    }

    @Transactional
    public List<CourseResponseDto> findAllCoursesByStudentId(UUID studentId) {
        List<Course> allCoursesByStudentId = courseRepository.findAllCoursesByUserId(studentId);

        return allCoursesByStudentId
                .stream()
                .map(this::mapCourseToDto)
                .toList();
    }

    public List<CourseResponseAdminDto> findAllCoursesWithId() {
        var response = courseRepository.findAllCourses()
                .stream()
                .map(this::mapCourseToAdminDto)
                .toList();

        if (response.isEmpty()) {
            return new ArrayList<>();
        }

        return response;
    }

    private CourseResponseAdminDto mapCourseToAdminDto(Course course) {
        CourseResponseAdminDto responseDto = modelMapper.map(course, CourseResponseAdminDto.class);

        return responseDto;
    }

    public CourseResponseAdminDto updateCourse(CourseResponseAdminDto courseDto, UUID id) {
        System.out.println(courseDto.getId());
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());

        var updatedCourse = courseRepository.save(course);
        var dto = modelMapper.map(updatedCourse, CourseResponseAdminDto.class);
    
        return dto;
    }

    public List<CourseResponseDto> findAllCoursesByPattern(String pattern) {
        var courses = courseRepository.findAllCoursesByPattern(pattern);
        if (courses.isEmpty()) {
            return new ArrayList<>();
        }

        return courses.stream()
                .map(this::mapCourseToDto)
                .toList();
    }

    @Transactional
    public void enrollUserInCourse(UUID courseId, UUID userId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Курс с ID " + courseId + " не найден"));
        User user = userService.findUserById(userId);
        course.getUsers().add(user);

        courseRepository.save(course);
    }

    public Boolean isUserEnrolledOnCourse(UUID userId, UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Курс с ID " + courseId + " не найден"));
        User user = userService.findUserById(userId);

        return course.getUsers().contains(user);
    }

    public Stream<Course> findAll() {
        return courseRepository.findAll().stream();
    }

    @Transactional
    public List<User> getUsersByCourseId(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        course.getUsers().size(); // Принудительно загружаем пользователей
        return new ArrayList<>(course.getUsers());
    }

}
