package com.k_plus.internship.ArticlePackage;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.ArticleNotFoundException;
import com.k_plus.internship.CoursePackage.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ModelMapper modelMapper;
    private final CourseService courseService;

    public ArticleResponseDto findArticleByIdReturningDto(UUID uuid) {
        return modelMapper.map(findArticleById(uuid), ArticleResponseDto.class);
    }

    public Article findArticleById(UUID uuid) {
        return articleRepository.findById(uuid).orElseThrow(
                () -> new ArticleNotFoundException("Article not found with id: " + uuid)
        );
    }

    @Transactional
    public ArticleResponseDto saveArticle(@Valid ArticleRequestDto articleRequestDto) {
        var article = modelMapper.map(articleRequestDto, Article.class);
        article.setId(Generators.timeBasedEpochGenerator().generate());
        article.setCourse(courseService.findCourseById(articleRequestDto.getCourseId()));
        articleRepository.save(article);

        return modelMapper.map(article, ArticleResponseDto.class);
    }

    @Transactional
    public void deleteArticleById(UUID id) {
        articleRepository.deleteById(id);
    }

    public List<ArticleResponseAdminDto> findAllByCourseId(UUID courseId) {
        List<Article> allArticlesbyCourseId = articleRepository.findAllByCourseId(courseId);

        return allArticlesbyCourseId
                .stream()
                .map(this::mapArticleToAdminDto)
                .toList();
    }

    private ArticleResponseAdminDto mapArticleToAdminDto(Article article) {
    ArticleResponseAdminDto responseDto = modelMapper.map(article, ArticleResponseAdminDto.class);

    return responseDto;
    }
}
