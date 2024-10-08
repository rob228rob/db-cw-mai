package com.k_plus.internship.ArticlePackage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Optional<Article> findByTitle(String title);

    Optional<Article> findByTitleLike(String title);

    @Query("SELECT a FROM Article a")
    List<Article> findAllByCourseId(@Param("courseId") UUID courseId);
}
