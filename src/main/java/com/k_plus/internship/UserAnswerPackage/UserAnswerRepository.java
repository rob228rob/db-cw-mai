package com.k_plus.internship.UserAnswerPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, UUID> {

    Optional<UserAnswer> findAllByUserId(UUID userId);

    @Query("""
    SELECT ua FROM UserAnswer ua 
    WHERE ua.user.id=:userId 
        AND ua.testing.id=:testId 
    ORDER BY ua.id""")
    List<UserAnswer> findAllByUserIdAndByTestIdOrderById(UUID userId, UUID testId);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM UserRating ur 
            WHERE ur.course.id = :courseId 
            AND ur.user.id = :userId
            AND ur.testing.id = :testId""")
    void deleteAllByUserIdAndTestingIdAndCourseId(UUID userId, UUID testId, UUID courseId);

    void deleteAllByUserIdAndTestingId(UUID userId, UUID testId);

}
