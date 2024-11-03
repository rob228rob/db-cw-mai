package com.mai.db_cw_rbl.SpecializationPackage.Dao;

import com.mai.db_cw_rbl.SpecializationPackage.Specialization;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SpecializationDaoImpl implements SpecializationDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Specialization> specializationRowMapper = specializationRowMapper();

    @Override
    public Optional<Specialization> findSpecializationById(UUID id) {
        String sql = "SELECT * FROM specializations WHERE id = ?";
        try {
            Specialization specialization = jdbcTemplate.queryForObject(sql, specializationRowMapper, id);
            return Optional.ofNullable(specialization);
        } catch (EmptyResultDataAccessException e) {
            // Запись не найдена
            return Optional.empty();
        }
    }

    @Override
    public boolean saveSpecialization(Specialization specialization) {
        String query = "INSERT INTO specializations (id, name, code) VALUES (?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(query,
                specialization.getId(),
                specialization.getName(),
                specialization.getCode());

        return rowsAffected == 1;
    }

    @Override
    public Optional<Specialization> findSpecializationByName(String name) {
        String sql = "SELECT * FROM specializations WHERE name = ?";
        try {
            Specialization specialization = jdbcTemplate.queryForObject(sql, specializationRowMapper, name);
            return Optional.ofNullable(specialization);
        } catch (EmptyResultDataAccessException e) {
            // Запись не найдена
            return Optional.empty();
        }
    }

    @Override
    public Optional<Specialization> findSpecializationByCode(String code) {
        String sql = "SELECT * FROM specializations WHERE code = ?";
        try {
            Specialization specialization = jdbcTemplate.queryForObject(sql, specializationRowMapper, code);
            return Optional.ofNullable(specialization);
        } catch (DataAccessException e) {
            // Запись не найдена
            return Optional.empty();
        }
    }

    @Override
    public List<Specialization> findAllSpecializations() {
        String sql = "SELECT * FROM specializations";
        return jdbcTemplate.query(sql, specializationRowMapper);
    }

    @Override
    public boolean existByName(String name) {
        String query = "SELECT * FROM specializations WHERE name = ?";

        var specialization = jdbcTemplate
                .query(query, specializationRowMapper, name)
                .stream()
                .findFirst();

        return specialization.isPresent();
    }

    private static RowMapper<Specialization> specializationRowMapper() {
        return (rs, rowNum) ->
                Specialization.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .name(rs.getString("name"))
                        .code(rs.getString("code"))
                        .build();
    }
}