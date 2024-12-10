package com.mai.db_cw_rbl.lawyer.dao;

import com.mai.db_cw_rbl.lawyer.Lawyer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LawyerDao {

    Optional<Lawyer> findById(UUID id);

    Optional<Lawyer> findByLicenceNumber(String licenceNumber);

    boolean existsByLicenceNumber(String licenceNumber);

    void save(Lawyer lawyer);

    void deleteById(UUID id);

    List<Lawyer> findAll();

    List<Lawyer> findAllWithBatch(int batchSize);

    Optional<Lawyer> findByUserId(UUID userId);
}