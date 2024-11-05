package com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dao;

import com.mai.db_cw_rbl.ConfirmedAnswersPackage.ConfirmedAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ConfirmedAnsDaoImpl implements ConfirmedAnsDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<ConfirmedAnswer> CONFIRMED_ANSWER_ROW_MAPPER =
            (rs, rowNum) -> ConfirmedAnswer.builder()
            .answerId(UUID.fromString(rs.getString("answer_id")))
            .id(UUID.fromString(rs.getString("id")))
            .questionId(UUID.fromString(rs.getString("question_id")))
            .build();

    /**
     * Находит подтверждённый ответ по идентификатору вопроса.
     *
     * @param questionId Идентификатор вопроса.
     * @return Optional с ConfirmedAnswer, если найдено, иначе пустой Optional.
     */
    @Override
    public Optional<ConfirmedAnswer> findByQuestionId(UUID questionId) {
        String sql = "SELECT * FROM confirmed_answers WHERE question_id = :questionId LIMIT 1";

        Map<String, Object> params = new HashMap<>();
        params.put("questionId", questionId);

        return namedParameterJdbcTemplate.query(sql, params, CONFIRMED_ANSWER_ROW_MAPPER)
                .stream().findFirst();
    }

    /**
     * Сохраняет новый подтверждённый ответ в базу данных.
     *
     * @param confirmedAnswer Объект ConfirmedAnswer для сохранения.
     * @return true, если сохранение прошло успешно, иначе false.
     */
    @Override
    public boolean save(ConfirmedAnswer confirmedAnswer) {
        String sql = "INSERT INTO confirmed_answers (id, answer_id, question_id) " +
                "VALUES (:id, :answerId, :questionId)";

        Map<String, Object> params = new HashMap<>();
        params.put("answerId", confirmedAnswer.getAnswerId());
        params.put("id", confirmedAnswer.getId());
        params.put("questionId", confirmedAnswer.getQuestionId());

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    /**
     * Удаляет подтверждённый ответ из базы данных.
     *
     * @param questionId UUID question id.
     * @return true, если удаление прошло успешно, иначе false.
     */
    @Override
    public boolean deleteByQuestionId(UUID questionId) {
        String sql = "DELETE FROM confirmed_answers WHERE question_id = :questionId";

        Map<String, Object> params = new HashMap<>();
        params.put("questionId", questionId);

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }

    @Override
    public boolean checkConfirmation(UUID answerId) {
        String sql = "SELECT * FROM confirmed_answers WHERE answer_id = :answerId";

        List<ConfirmedAnswer> answerList = namedParameterJdbcTemplate.query(sql, Map.of("answerId", answerId), CONFIRMED_ANSWER_ROW_MAPPER);
        return !answerList.isEmpty();
    }
}
