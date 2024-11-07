package com.mai.db_cw_rbl.RatingPackage.Dao;

import com.mai.db_cw_rbl.RatingPackage.Dto.AnswerRating;
import com.mai.db_cw_rbl.RatingPackage.LawyerRating;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LawyerRatingDaoImpl implements LawyerRatingDao {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private static final RowMapper<LawyerRating> RATING_ROW_MAPPER = (rs, c) -> {
        return LawyerRating.builder()
                .id(UUID.fromString(rs.getString("id")))
                .questionId(UUID.fromString(rs.getString("question_id")))
                .lawyerId(UUID.fromString(rs.getString("lawyer_id")))
                //.createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .comment(rs.getString("comment"))
                .rating(rs.getInt("rating"))
                .build();
    };

    @Override
    public Optional<LawyerRating> findRatingById(UUID uuid) {
        String sql = "SELECT * FROM lawyer_ratings WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", uuid);

        return namedJdbcTemplate.query(sql, params, RATING_ROW_MAPPER)
                .stream()
                .findFirst();
    }

    @Override
    public boolean saveRating(LawyerRating lawyerRating) {
        String sql = """
                INSERT INTO lawyer_ratings
                (id, question_id, lawyer_id, comment, rating)
                VALUES (:id, :questionId, :lawyerId, :comment, :rating)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", lawyerRating.getId())
                .addValue("questionId", lawyerRating.getQuestionId())
                .addValue("lawyerId", lawyerRating.getLawyerId())
                .addValue("comment", lawyerRating.getComment())
                .addValue("rating", lawyerRating.getRating());
//                .addValue("created_at",
//                        lawyerRating.getCreatedAt() == null
//                                ? LocalDateTime.now()
//                                : lawyerRating.getCreatedAt());

        int rowAffected = namedJdbcTemplate.update(sql, params);

        return rowAffected > 0;
    }

    @Override
    public Optional<LawyerRating> findRatingBAnswerId(UUID answerId) {
        String sql = """
                SELECT * FROM lawyer_ratings 
                WHERE answer_id = :answerId""";
        MapSqlParameterSource params = new MapSqlParameterSource("answerId", answerId);

        return namedJdbcTemplate.query(sql, params, RATING_ROW_MAPPER)
                .stream().
                findFirst();
    }


    //TODO: in service layer CALC AVG double RATING!
    @Override
    public List<Double> findAllRatingsByLawyerId(UUID lawyerId) {
        String sql = """
                SELECT lr.rating FROM lawyer_ratings lr
                WHERE lr.lawyer_id = :lawyerId""";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("lawyerId", lawyerId);

        return namedJdbcTemplate.query(sql, params, (rs, c) -> {
            return rs.getDouble("rating");
        });
    }

    @Override
    public String findCommentByQuestionId(UUID questionId) {
        String sql = """
                SELECT lr.comment 
                FROM lawyer_ratings lr 
                WHERE lr.question_id = :questionId""";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("questionId", questionId);

        return namedJdbcTemplate.query(sql, params, (rs, c) -> rs.getString("comment"))
                .stream().findFirst().orElse("");
    }

}
