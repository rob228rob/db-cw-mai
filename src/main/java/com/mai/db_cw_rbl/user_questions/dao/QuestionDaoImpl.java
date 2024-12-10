package com.mai.db_cw_rbl.user_questions.dao;

import com.mai.db_cw_rbl.user_questions.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class QuestionDaoImpl implements QuestionDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Question> questionRowMapper = (rs, rowNum) -> {
        Question question = new Question();
        question.setId(UUID.fromString(rs.getString("id")));
        question.setUserId(UUID.fromString(rs.getString("user_id")));
        question.setAnswered(rs.getBoolean("answered"));
        question.setQuestionText(rs.getString("question_text"));
        question.setQuestionTitle(rs.getString("question_title"));
        question.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        //question.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return question;
    };

    @Override
    public Optional<Question> findByQuestionId(UUID uuid) {
        String sql = "SELECT * FROM user_questions WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", uuid);
        List<Question> questions = namedParameterJdbcTemplate.query(sql, params, questionRowMapper);
        return questions.stream().findFirst();
    }

    @Override
    public Optional<Question> findByTitle(String title) {
        String sql = "SELECT * FROM user_questions WHERE question_title = :title";
        MapSqlParameterSource params = new MapSqlParameterSource("title", title);
        List<Question> questions = namedParameterJdbcTemplate.query(sql, params, questionRowMapper);
        return questions.stream().findFirst();
    }

    @Override
    public List<Question> findAllByUserId(UUID userId) {
        String sql = "SELECT * FROM user_questions WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
        return namedParameterJdbcTemplate.query(sql, params, questionRowMapper);
    }

    @Override
    public boolean saveQuestion(Question question) {
        String sql = """
                INSERT INTO user_questions 
                (id, user_id, question_text, question_title, created_at, updated_at) 
                VALUES (:id, :userId, :questionText, :questionTitle, :createdAt, :updatedAt)""";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", question.getId())
                .addValue("userId", question.getUserId())
                .addValue("questionText", question.getQuestionText())
                .addValue("questionTitle", question.getQuestionTitle())
                .addValue("createdAt", question.getCreatedAt())
                .addValue("updatedAt", question.getUpdatedAt());
        return namedParameterJdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public boolean deleteQuestion(UUID questionId, UUID userId) {
        String sql = "DELETE FROM user_questions WHERE id = :questionId AND user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("questionId", questionId)
                .addValue("userId", userId);

        return namedParameterJdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public boolean updateQuestion(Question question) {
        String sql = "UPDATE user_questions SET question_text = :questionText, question_title = :questionTitle, updated_at = :updatedAt WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", question.getId())
                .addValue("questionText", question.getQuestionText())
                .addValue("questionTitle", question.getQuestionTitle())
                .addValue("updatedAt", question.getUpdatedAt());
        return namedParameterJdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public List<Question> findAllQuestions() {
        String sql = """
            SELECT * FROM user_questions ORDER BY created_at DESC""";

        return namedParameterJdbcTemplate.query(sql, questionRowMapper);
    }

    @Override
    public void updateAnswered(boolean answered, UUID id) {
        String sql = "UPDATE user_questions SET answered = :answered WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("answered", answered)
                .addValue("id", id);

        int rowAffected = namedParameterJdbcTemplate.update(sql, params);

    }
}
