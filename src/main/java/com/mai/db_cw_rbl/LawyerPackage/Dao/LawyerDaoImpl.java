package com.mai.db_cw_rbl.LawyerPackage.Dao;

import com.mai.db_cw_rbl.LawyerPackage.Lawyer;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LawyerDaoImpl implements LawyerDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Lawyer> lawyerRowMapper = new LawyerRowMapper();

    @Override
    public Optional<Lawyer> findById(UUID id) {
        String query = "SELECT * FROM lawyers WHERE id = ?";
        try {
            Lawyer lawyer = jdbcTemplate.queryForObject(query, lawyerRowMapper, id);
            return Optional.ofNullable(lawyer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Lawyer> findByLicenceNumber(String licenceNumber) {
        String query = "SELECT * FROM lawyers WHERE licence_number = ?";
        try {
            Lawyer lawyer = jdbcTemplate.queryForObject(query, lawyerRowMapper, licenceNumber);
            return Optional.ofNullable(lawyer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByLicenceNumber(String licenceNumber) {
        String query = "SELECT COUNT(*) FROM lawyers WHERE licence_number = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, licenceNumber);
        return count != null && count > 0;
    }

    @Override
    public void save(Lawyer lawyer) {
        String query = """
            insert into lawyers 
            (id, 
             user_id, 
             specialization_id, 
             years_experience, 
             licence_number) 
            values (?, ?, ?, ?, ?)""";
        jdbcTemplate.update(query, lawyer.getId(), lawyer.getUserId(),
                lawyer.getSpecializationId(), lawyer.getYearsExperience(), lawyer.getLicenceNumber());
    }

    @Override
    public void deleteById(UUID id) {
        String query = "DELETE FROM lawyers WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Lawyer> findAll() {
        String query = "SELECT * FROM lawyers";
        return jdbcTemplate.query(query, lawyerRowMapper);
    }

    @Override
    public List<Lawyer> findAllWithBatch(int batchSize) {
        String query = "SELECT * FROM lawyers FETCH FIRST ? ROWS ONLY";

        return jdbcTemplate.query(query, lawyerRowMapper, batchSize);
    }

    @Override
    public Optional<Lawyer> findByUserId(UUID userId) {
        var query = "SELECT * FROM lawyers WHERE user_id = ?";

        return jdbcTemplate.query(query, lawyerRowMapper, userId)
                .stream()
                .findFirst();
    }

    private static class LawyerRowMapper implements RowMapper<Lawyer> {
        @Override
        public Lawyer mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = UUID.fromString(rs.getString("id"));
            UUID userId = UUID.fromString(rs.getString("user_id"));
            String licenceNumber = rs.getString("licence_number");
            int yearsExperience = rs.getInt("years_experience");
            UUID specializationId = null;
            String specializationIdStr = rs.getString("specialization_id");
            if (specializationIdStr != null) {
                specializationId = UUID.fromString(specializationIdStr);
            }
            return new Lawyer(id, userId, specializationId, yearsExperience, licenceNumber);
        }
    }
}