package com.k_plus.internship.CertificatePackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {

    Optional<Certificate> findAllByUserEmail(String userEmail);

    @Query("SELECT c.sent FROM Certificate c WHERE c.userEmail=:email AND c.course.id = :courseId")
    Optional<Boolean> isAlreadySent(String email, UUID courseId);
}
