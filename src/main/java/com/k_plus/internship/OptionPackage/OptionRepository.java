package com.k_plus.internship.OptionPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptionRepository extends JpaRepository<Option, UUID> {

    Optional<Option> findById(UUID id);

    @Query("SELECT o.correct FROM Option o WHERE o.id = :uuid")
    boolean optionIsCorrect(UUID uuid);
}
