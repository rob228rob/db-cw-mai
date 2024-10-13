package com.k_plus.internship.TestingPackage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestingRepository extends JpaRepository<Testing, UUID> {

    Optional<Testing> findByName(String name);

    @Query("SELECT t FROM Testing t WHERE t.course.id = :courseId ORDER BY t.displayOrder")
    List<Testing> findAllTestsByCourseId(UUID courseId);
}
