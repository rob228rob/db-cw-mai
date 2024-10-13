package com.k_plus.internship.QuestionPackage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    Optional<Question> findByQuestionText(String questionText);

    @Query("SELECT q FROM Question q WHERE q.testing.id = :testingId")
    List<Question> findAllQuestionsByTestingId(UUID testingId);
}
