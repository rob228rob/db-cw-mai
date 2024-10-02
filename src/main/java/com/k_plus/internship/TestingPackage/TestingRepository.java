package com.k_plus.internship.TestingPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestingRepository extends JpaRepository<Testing, UUID> {

    Optional<Testing> findByName(String name);
}
