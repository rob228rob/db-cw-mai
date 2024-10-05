package com.k_plus.internship.CoursePackage;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @NotNull Optional<Course> findById(@NotNull UUID id);

    @Query("SELECT c FROM Course c WHERE c.id IS NOT NULL")
    List<Course> findAllCourses();

    boolean existsByName(String name);

    @Query("SELECT c FROM Course c JOIN c.users u WHERE u.id = :userId")
    List<Course> findAllCoursesByUserId(UUID userId);
}
