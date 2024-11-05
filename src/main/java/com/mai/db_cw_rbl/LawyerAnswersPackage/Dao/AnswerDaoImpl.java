package com.mai.db_cw_rbl.LawyerAnswersPackage.Dao;

import com.mai.db_cw_rbl.LawyerAnswersPackage.Answer;
import com.mai.db_cw_rbl.LawyerAnswersPackage.Dto.AnswerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AnswerDaoImpl implements AnswerDao {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private static final RowMapper<Answer> answerRowMapper = (rs, rowNum) -> Answer.builder()
            .lawyerId(UUID.fromString(rs.getString("lawyer_id")))
            .questionId(UUID.fromString(rs.getString("question_id")))
            .id(UUID.fromString(rs.getString("id")))
            .answer(rs.getString("answer_text"))
            .createdAt(rs.getTimestamp("answered_at").toLocalDateTime())
           // .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();

    @Override
    public Optional<Answer> findById(UUID id) {
        String sql = "SELECT * FROM lawyer_answers WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return namedJdbcTemplate.query(sql, params, answerRowMapper).stream().findFirst();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Answer> findAllByLawyerId(UUID lawyerId) {
        String sql = "SELECT * FROM lawyer_answers WHERE lawyer_id = :lawyerId";
        MapSqlParameterSource params = new MapSqlParameterSource("lawyerId", lawyerId);
        return namedJdbcTemplate.query(sql, params, answerRowMapper);
    }

    @Override
    public boolean save(Answer answer) {
        String sql = "INSERT INTO lawyer_answers (id, lawyer_id, question_id, answer_text, answered_at) " +
                "VALUES (:id, :lawyerId, :questionId, :answer, :createdAt)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", answer.getId())
                .addValue("lawyerId", answer.getLawyerId())
                .addValue("questionId", answer.getQuestionId())
                .addValue("answer", answer.getAnswer())
                .addValue("createdAt", answer.getCreatedAt());

        return namedJdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public boolean delete(Answer answer) {
        String sql = "DELETE FROM lawyer_answers WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", answer.getId());
        return namedJdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public boolean update(Answer answer) {
        String sql = """
                UPDATE lawyer_answers 
                SET lawyer_id = :lawyerId, question_id = :questionId, answer = :answer,
                updated_at = :updatedAt WHERE id = :id""";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", answer.getId())
                .addValue("lawyerId", answer.getLawyerId())
                .addValue("questionId", answer.getQuestionId())
                .addValue("answer", answer.getAnswer())
                .addValue("updatedAt", answer.getUpdatedAt());

        return namedJdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public List<Answer> findAllByQuestionId(UUID questionId) {
        String sql = "SELECT * FROM lawyer_answers WHERE question_id = :questionId";
        MapSqlParameterSource params = new MapSqlParameterSource("questionId", questionId);
        return namedJdbcTemplate.query(sql, params, answerRowMapper);
    }

    @Override
    public List<AnswerResponse> findAllByQuestionIdWithUserData(UUID questionId) {
        String sql = """
                SELECT la.*, u.first_name, u.last_name FROM lawyer_answers la 
                LEFT JOIN lawyers l ON la.lawyer_id = l.id 
                LEFT JOIN users u ON l.user_id = u.id 
                WHERE la.question_id = :questionId""";
        SqlParameterSource params = new MapSqlParameterSource("questionId", questionId);

        return namedJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AnswerResponse answerResponse = new AnswerResponse();
            answerResponse.setId(UUID.fromString(rs.getString("id")));
            answerResponse.setAnswer(rs.getString("answer_text"));
            answerResponse.setLawyerId(rs.getString("lawyer_id"));
            answerResponse.setCreatedAt(rs.getTimestamp("answered_at").toLocalDateTime());

            AnswerResponse.LawyerData lawyerName = new AnswerResponse.LawyerData(
                    rs.getString("first_name"),
                    rs.getString("last_name")
            );
            answerResponse.setLawyerData(lawyerName);

            return answerResponse;
        });
    }
}

