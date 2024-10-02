package com.k_plus.internship.ArticlePackage;

import com.k_plus.internship.CommonPackage.CustomExceptions.ArticleNotFoundException;
import com.k_plus.internship.CoursePackage.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ModelMapper modelMapper;

    public ArticleResponseDto findArticleByIdReturningDto(UUID uuid) {
        return modelMapper.map(findArticleById(uuid), ArticleResponseDto.class);
    }

    public Article findArticleById(UUID uuid) {
        return articleRepository.findById(uuid).orElseThrow(
                () -> new ArticleNotFoundException("Article not found with id: " + uuid)
        );
    }

    public ArticleResponseDto saveArticle(@Valid ArticleRequestDto articleRequestDto) {
        articleRepository.save(modelMapper.map(articleRequestDto, Article.class));
        return modelMapper.map(articleRequestDto, ArticleResponseDto.class);
    }

    public void deleteArticleById(UUID id) {
    }
}
