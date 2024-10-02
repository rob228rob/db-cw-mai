package com.k_plus.internship.CoursePackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.ArticlePackage.Article;
import com.k_plus.internship.ArticlePackage.ArticleService;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.TestingPackage.TestingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final ModelMapper modelMapper;

    private final ArticleService articleService;

    private final TestingService testingService;

    private final CourseRepository courseRepository;

    public CourseResponseDto saveCourse(CourseRequestDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);
        course.setId(Generators.timeBasedEpochGenerator().generate());
        //Additional logic if it's required
        course.setArticles(mapIdToArticles(courseDto.getArticleIds()));
        course.setTestings(mapIdToTestings(courseDto.getTestIds()));

        var dto = modelMapper.map(courseRepository.save(course), CourseResponseDto.class);
        dto.setArticleIds(courseDto.getArticleIds());
        dto.setTestingIds(courseDto.getTestIds());

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

    private Collection<Article> mapIdToArticles(List<UUID> articleIds) {
        if (articleIds.isEmpty()) {
            return new ArrayList<>();
        }

        return articleIds
                .stream()
                .map(articleService::findArticleById)
                .toList();
    }
}
