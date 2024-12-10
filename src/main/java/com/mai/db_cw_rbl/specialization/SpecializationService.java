package com.mai.db_cw_rbl.specialization;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.infrastructure.exceptions.EntityNotFoundException;
import com.mai.db_cw_rbl.specialization.dao.SpecializationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecializationService {

    private final SpecializationDao specializationDao;

    public boolean saveSpecialization(String name) {
        String code = name.trim()
                .replace(' ', '_')
                .toUpperCase();
        var uuid = Generators.timeBasedGenerator().generate();
        Specialization specialization = new Specialization(uuid, name, code);
        return specializationDao.saveSpecialization(specialization);
    }

    public boolean saveSpecialization(Specialization specialization) {
        return specializationDao.saveSpecialization(specialization);
    }

    public Specialization findSpecializationByName(String name) {
        return specializationDao.findSpecializationByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Specialization with name " + name + " not found"));
    }

    public Specialization findSpecializationById(UUID specializationId) {
        return specializationDao.findSpecializationById(specializationId)
                .orElseThrow(() -> new EntityNotFoundException("Specialization with id " + specializationId + " not found"));
    }

    public boolean existsSpecializationByName(String name) {
        return specializationDao.existByName(name);
    }
}
