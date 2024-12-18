package com.mai.db_cw_rbl.specialization.dao;

import com.mai.db_cw_rbl.specialization.Specialization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecializationDao {

    Optional<Specialization> findSpecializationById(UUID id);

    boolean saveSpecialization(Specialization specialization);

    Optional<Specialization> findSpecializationByName(String name);

    Optional<Specialization> findSpecializationByCode(String code);

    List<Specialization> findAllSpecializations();

    boolean existByName(String name);
}
