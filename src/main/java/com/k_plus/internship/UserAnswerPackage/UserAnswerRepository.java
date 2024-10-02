package com.k_plus.internship.UserAnswerPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, UUID> {

    Optional<UserAnswer> findAllByUserId(UUID userId);
}
