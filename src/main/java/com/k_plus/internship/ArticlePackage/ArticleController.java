package com.k_plus.internship.ArticlePackage;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/get/{id}")
    public ResponseEntity<ArticleResponseDto> getArticleById(@PathVariable UUID id) {
        var articleDto = articleService.findArticleByIdReturningDto(id);

        return ResponseEntity.ok(articleDto);
    }

    @PostMapping("/add")
    public ResponseEntity<ArticleResponseDto> addArticle(
            @RequestBody @Valid ArticleRequestDto articleRequestDto) {
        return ResponseEntity
                .ok(articleService.saveArticle(articleRequestDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ArticleResponseDto> deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticleById(id);

        return ResponseEntity
                .noContent().build();
    }
}
