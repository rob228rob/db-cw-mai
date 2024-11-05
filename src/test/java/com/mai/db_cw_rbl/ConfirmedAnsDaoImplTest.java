package com.mai.db_cw_rbl;

import com.mai.db_cw_rbl.ConfirmedAnswersPackage.ConfirmedAnswer;
import com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dao.ConfirmedAnsDao;
import com.mai.db_cw_rbl.ConfirmedAnswersPackage.Dao.ConfirmedAnsDaoImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@Import(ConfirmedAnsDaoImpl.class) // Импортируем реализацию DAO
//@Sql(scripts = "/schema.sql") // Загружаем схему базы данных перед тестами
@ActiveProfiles("test") // Активируем тестовый профиль
public class ConfirmedAnsDaoImplTest {

    @Autowired
    private ConfirmedAnsDao confirmedAnsDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Метод для создания записи в таблице users.
     */
    private UUID createTestUser(String name, String email, String password) {
        UUID userId = UUID.randomUUID();
        String sql = "INSERT INTO users (id, first_name, last_name, email, password) VALUES (:id, :firstName, :lastName, :email, :password)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId.toString());
        params.put("firstName", name.substring(0, name.indexOf(" ")));
        params.put("lastName", name.substring(name.indexOf(" ") + 1));
        params.put("email", email);
        params.put("password", password);
        namedParameterJdbcTemplate.update(sql, params);
        return userId;
    }

    /**
     * Метод для создания записи в таблице lawyers.
     */
    private UUID createTestLawyer(UUID userId, UUID specializationId, int yearsExperience, String licenceNumber) {
        UUID lawyerId = UUID.randomUUID();
        String sql = "INSERT INTO lawyers (id, user_id, specialization_id, years_experience, licence_number) VALUES (:id, :userId, :specializationId, :yearsExperience, :licenceNumber)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", lawyerId.toString());
        params.put("userId", userId.toString());
        params.put("specializationId", specializationId.toString());
        params.put("yearsExperience", yearsExperience);
        params.put("licenceNumber", licenceNumber);
        namedParameterJdbcTemplate.update(sql, params);
        return lawyerId;
    }

    /**
     * Метод для создания записи в таблице user_questions.
     */
    private UUID createTestUserQuestion(UUID userId, String questionTitle, String questionText) {
        UUID questionId = UUID.randomUUID();
        String sql = "INSERT INTO user_questions (id, user_id, question_title, question_text) VALUES (:id, :userId, :questionTitle, :questionText)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", questionId.toString());
        params.put("userId", userId.toString());
        params.put("questionTitle", questionTitle);
        params.put("questionText", questionText);
        namedParameterJdbcTemplate.update(sql, params);
        return questionId;
    }

    /**
     * Метод для создания записи в таблице lawyer_answers.
     */
    private UUID createTestLawyerAnswer(UUID lawyerId, UUID questionId, String answerText) {
        UUID answerId = UUID.randomUUID();
        String sql = "INSERT INTO lawyer_answers (id, lawyer_id, question_id, answer_text) VALUES (:id, :lawyerId, :questionId,:answerText)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", answerId);
        params.put("lawyerId", lawyerId);
        params.put("questionId", questionId);
        params.put("title", answerText);
        params.put("answerText", answerText);
        namedParameterJdbcTemplate.update(sql, params);
        return answerId;
    }

    /**
     * Метод для создания записи в таблице confirmed_answers.
     * В реальных тестах этот метод, скорее всего, не нужен, так как ConfirmedAnsDaoImpl должен это делать.
     * Однако, если вы хотите напрямую вставлять записи для проверки, можно использовать его.
     */
    private UUID createTestConfirmedAnswer(UUID answerId, UUID questionId) {
        UUID confirmedAnswerId = UUID.randomUUID();
        String sql = "INSERT INTO confirmed_answers (id, answer_id, question_id) VALUES (:id, :answerId, :questionId)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", confirmedAnswerId.toString());
        params.put("answerId", answerId.toString());
        params.put("questionId", questionId.toString());
        namedParameterJdbcTemplate.update(sql, params);
        return confirmedAnswerId;
    }

    @Test
    @DisplayName("Тест метода findByQuestionId - запись существует")
    void testFindByQuestionIdExists() {

        UUID userId = createTestUser("John Doe", "john.doe@example.com", "password123");

        UUID specializationId = UUID.randomUUID();
        String specializationName = "Criminal Law";
        String sqlSpecialization = "INSERT INTO specializations (id, name, code) VALUES (:id, :name, :code)";
        Map<String, Object> paramsSpecialization = new HashMap<>();
        paramsSpecialization.put("id", specializationId.toString());
        paramsSpecialization.put("name", specializationName);
        paramsSpecialization.put("code", specializationName.toUpperCase().trim().replace(' ', '_'));
        namedParameterJdbcTemplate.update(sqlSpecialization, paramsSpecialization);

        UUID lawyerId = createTestLawyer(userId, specializationId, 10, "LIC123456");

        UUID questionId = createTestUserQuestion(userId, "Title!!!", "What is your favorite color?");

        UUID answerId = createTestLawyerAnswer(lawyerId, questionId,"This is a test answer.");


        UUID confirmedAnswerId = createTestConfirmedAnswer(answerId, questionId);
        ConfirmedAnswer confirmedAnswer = ConfirmedAnswer.builder()
                .answerId(answerId)
                .id(confirmedAnswerId)
                .questionId(questionId)
                .build();

        Optional<ConfirmedAnswer> result = confirmedAnsDao.findByQuestionId(questionId);

        // Assert: проверяем, что запись найдена и соответствует ожиданиям
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(confirmedAnswer);
    }

//
//    @Test
//    @DisplayName("Тест метода findByQuestionId - запись не существует")
//    void testFindByQuestionIdNotExists() {
//        // Arrange: создаём уникальный questionId, которого нет в базе
//        UUID questionId = UUID.randomUUID();
//
//        // Act: ищем по questionId
//        Optional<ConfirmedAnswer> result = confirmedAnsDao.findByQuestionId(questionId);
//
//        // Assert: проверяем, что запись не найдена
//        assertThat(result).isNotPresent();
//    }
//
//    @Test
//    @DisplayName("Тест метода save - успешное сохранение")
//    void testSaveSuccessful() {
//        // Arrange: создаём тестовую запись в таблице user_questions
//        UUID questionId = createTestUserQuestion("What is your favorite programming language?");
//        UUID answerId = UUID.randomUUID();
//        UUID id = UUID.randomUUID();
//
//        ConfirmedAnswer confirmedAnswer = ConfirmedAnswer.builder()
//                .answerId(answerId)
//                .id(id)
//                .questionId(questionId)
//                .build();
//
//        // Act: сохраняем запись
//        boolean isSaved = confirmedAnsDao.save(confirmedAnswer);
//
//        // Assert: проверяем, что сохранение прошло успешно
//        assertThat(isSaved).isTrue();
//
//        // Дополнительно: проверяем, что запись действительно сохранена
//        Optional<ConfirmedAnswer> retrieved = confirmedAnsDao.findByQuestionId(questionId);
//        assertThat(retrieved).isPresent();
//        assertThat(retrieved.get()).isEqualToComparingFieldByField(confirmedAnswer);
//    }
//
//    @Test
//    @DisplayName("Тест метода save - неудачное сохранение (дублирование PK)")
//    void testSaveFailureDuplicatePK() {
//        // Arrange: создаём тестовую запись в таблице user_questions
//        UUID questionId = createTestUserQuestion("What is your favorite food?");
//        UUID answerId = UUID.randomUUID();
//        UUID id1 = UUID.randomUUID();
//
//        ConfirmedAnswer confirmedAnswer1 = ConfirmedAnswer.builder()
//                .answerId(answerId)
//                .id(id1)
//                .questionId(questionId)
//                .build();
//
//        ConfirmedAnswer confirmedAnswer2 = ConfirmedAnswer.builder()
//                .answerId(answerId) // тот же answerId, предполагаем, что это UNIQUE
//                .id(UUID.randomUUID())
//                .questionId(questionId)
//                .build();
//
//        // Act: сохраняем первую запись
//        boolean isSaved1 = confirmedAnsDao.save(confirmedAnswer1);
//        // Пытаемся сохранить вторую запись с тем же answerId
//        boolean isSaved2 = false;
//        try {
//            isSaved2 = confirmedAnsDao.save(confirmedAnswer2);
//        } catch (DataIntegrityViolationException e) {
//            // Ожидаем, что произойдёт исключение из-за нарушения уникальности
//            isSaved2 = false;
//        }
//
//        // Assert: первая запись успешно сохранена, вторая — нет
//        assertThat(isSaved1).isTrue();
//        assertThat(isSaved2).isFalse();
//    }
//
//    @Test
//    @DisplayName("Тест метода deleteByQuestionId - успешное удаление")
//    void testDeleteByQuestionIdSuccessful() {
//        // Arrange: создаём тестовую запись в таблице user_questions
//        UUID questionId = createTestUserQuestion("What is your favorite movie?");
//        UUID answerId = UUID.randomUUID();
//        UUID id = UUID.randomUUID();
//
//        ConfirmedAnswer confirmedAnswer = ConfirmedAnswer.builder()
//                .answerId(answerId)
//                .id(id)
//                .questionId(questionId)
//                .build();
//
//        confirmedAnsDao.save(confirmedAnswer);
//
//        // Act: удаляем запись по questionId
//        boolean isDeleted = confirmedAnsDao.deleteByQuestionId(questionId);
//
//        // Assert: проверяем, что удаление прошло успешно
//        assertThat(isDeleted).isTrue();
//
//        // Дополнительно: проверяем, что запись действительно удалена
//        Optional<ConfirmedAnswer> retrieved = confirmedAnsDao.findByQuestionId(questionId);
//        assertThat(retrieved).isNotPresent();
//    }
//
//    @Test
//    @DisplayName("Тест метода deleteByQuestionId - удаление несуществующей записи")
//    void testDeleteByQuestionIdNotExists() {
//        // Arrange: создаём уникальный questionId, которого нет в базе
//        UUID questionId = UUID.randomUUID();
//
//        // Act: пытаемся удалить запись по questionId
//        boolean isDeleted = confirmedAnsDao.deleteByQuestionId(questionId);
//
//        // Assert: проверяем, что удаление не произошло
//        assertThat(isDeleted).isFalse();
//    }
//
//    @Test
//    @DisplayName("Тест метода deleteByQuestionId - удаление с Null")
//    void testDeleteByQuestionIdNull() {
//
//        // Act & Assert: ожидание исключения при передаче null
//        assertThrows(IllegalArgumentException.class, () -> {
//            confirmedAnsDao.deleteByQuestionId(null);
//        });
//    }
}
