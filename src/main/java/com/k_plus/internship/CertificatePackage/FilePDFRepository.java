package com.k_plus.internship.CertificatePackage;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilePDFRepository extends JpaRepository<FilePDF, UUID> {

}
