package com.k_plus.internship.ArticlePackage;

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
    @PostMapping("/create")
    public ResponseEntity<ArticleResponseDto> createArticle(
            @RequestBody @Valid ArticleRequestDto articleRequestDto) {
        return ResponseEntity
                .ok(articleService.saveArticle(articleRequestDto));
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ArticleResponseDto> deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticleById(id);

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/get-all/{uuid}")
    public ResponseEntity<List<ArticleResponseAdminDto>> findAllByCourseID(@PathVariable UUID uuid) {
        return ResponseEntity
                .ok(articleService.findAllByCourseId(uuid));
    }
}
