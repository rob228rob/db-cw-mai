package com.k_plus.internship.ArticlePackage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;
/*

/api/v1/articles/get?id=12erty3456

 */
    @GetMapping("/get/{id}")
    public ResponseEntity<ArticleResponseDto> getArticleById(@PathVariable UUID id) {
        var articleDto = articleService.findArticleByIdReturningDto(id);

        return ResponseEntity.ok(articleDto);
    }
/*
/api/v1/articles/add
 */
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
