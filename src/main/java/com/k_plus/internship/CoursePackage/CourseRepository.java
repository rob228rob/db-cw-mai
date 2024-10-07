package com.k_plus.internship.CoursePackage;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @NotNull Optional<Course> findById(@NotNull UUID id);

    @Query("SELECT c FROM Course c WHERE c.id IS NOT NULL ORDER BY c.id")
    List<Course> findAllCourses();

    boolean existsByName(String name);

    @Query("SELECT c FROM Course c JOIN c.users u WHERE u.id = :userId ORDER BY c.id")
    List<Course> findAllCoursesByUserId(UUID userId);

    @Query("""
    SELECT c FROM Course c 
    WHERE UPPER(c.name) LIKE CONCAT('%', UPPER(:pattern), '%') 
    OR UPPER(c.description) LIKE CONCAT('%', UPPER(:pattern), '%')
    ORDER BY c.name DESC, c.id ASC """)
    List<Course> findAllCoursesByPattern(@Param("pattern") String pattern);


    List<Course> findAllByNameLikeIgnoreCase(String name);
}
