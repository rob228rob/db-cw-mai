package com.k_plus.internship.ArticlePackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Optional<Article> findByTitle(String title);

    Optional<Article> findByTitleLike(String title);
}
