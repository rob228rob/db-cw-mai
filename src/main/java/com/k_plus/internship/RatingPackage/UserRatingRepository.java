package com.k_plus.internship.RatingPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, UUID> {

    Optional<UserRating> findUserRatingById(UUID id);

    Optional<UserRating> findByUserIdAndTestingId(UUID userId, UUID courseId);

    @Query("""
        SELECT ur.correctCount FROM UserRating ur
        WHERE ur.user.id = :userId
            AND ur.course.id = :courseId""")
    long findCorrectCountByCourseIdAndUserId(UUID userId, UUID courseId);

    @Query("""
        SELECT count(rating) + 1 
        FROM UserRating AS rating 
        WHERE rating.course.id = :courseId 
            AND rating.correctCount > 
            (SELECT r.correctCount 
             FROM UserRating AS r 
             WHERE r.course.id = :courseId
                AND r.user.id = :userId)""")
    long calcUserRatingByCourseId(UUID userId, UUID courseId);

    @Query("""
    SELECT COUNT(DISTINCT ur.user.id) + 1 
    FROM UserRating ur 
    WHERE ur.course.id = :courseId 
    AND (SELECT SUM(innerUr.correctCount) 
         FROM UserRating innerUr 
         WHERE innerUr.user.id = ur.user.id 
         AND innerUr.course.id = :courseId) > 
        (SELECT SUM(innerUr2.correctCount) 
         FROM UserRating innerUr2 
         WHERE innerUr2.user.id = :userId
         AND innerUr2.course.id = :courseId)""")
    long calculateUserRatingByUserIdAndCourseId(UUID userId, UUID courseId);

    boolean existsByUserIdAndTestingId(UUID userId, UUID testingId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserRating ur WHERE ur.user.id = :userId AND ur.course.id = :courseId")
    void deleteByUserIdAndCourseId(UUID userId, UUID courseId);
}
