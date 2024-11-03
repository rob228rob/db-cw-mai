//package com.mai.db_cw_rbl;
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.mai.db_cw_rbl.UserQuestionPackage.Dao.QuestionDaoImpl;
//import com.mai.db_cw_rbl.UserQuestionPackage.Question;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//public class QuestionDaoTest {
//
//    @Autowired
//    private QuestionDaoImpl questionDao;
//
//    private Question testQuestion;
//
//    @BeforeEach
//    public void setup() {
//        testQuestion = new Question();
//        testQuestion.setId(UUID.randomUUID());
//        testQuestion.setUserId(UUID.randomUUID());
//        testQuestion.setLawyerId(UUID.randomUUID());
//        testQuestion.setQuestionTitle("Sample Question Title");
//        testQuestion.setQuestionText("This is a sample question.");
//        testQuestion.setCreatedAt(LocalDateTime.now());
//        testQuestion.setUpdatedAt(LocalDateTime.now());
//
//        questionDao.saveQuestion(testQuestion);
//    }
//
//    @Test
//    public void testFindByQuestionId() {
//        Optional<Question> foundQuestion = questionDao.findByQuestionId(testQuestion.getId());
//        assertTrue(foundQuestion.isPresent(), "Question should be found");
//        assertEquals(testQuestion.getQuestionTitle(), foundQuestion.get().getQuestionTitle());
//    }
//
//    @Test
//    public void testFindByTitle() {
//        Optional<Question> foundQuestion = questionDao.findByTitle(testQuestion.getQuestionTitle());
//        assertTrue(foundQuestion.isPresent(), "Question should be found by title");
//        assertEquals(testQuestion.getQuestionText(), foundQuestion.get().getQuestionText());
//    }
//
//    @Test
//    public void testFindAllByUserId() {
//        List<Question> questions = questionDao.findAllByUserId(testQuestion.getUserId());
//        assertFalse(questions.isEmpty(), "There should be at least one question for this user");
//    }
//
//    @Test
//    public void testUpdateQuestion() {
//        testQuestion.setQuestionText("Updated question text.");
//        boolean isUpdated = questionDao.updateQuestion(testQuestion);
//        assertTrue(isUpdated, "Question should be updated successfully");
//
//        Optional<Question> updatedQuestion = questionDao.findByQuestionId(testQuestion.getId());
//        assertTrue(updatedQuestion.isPresent(), "Updated question should be found");
//        assertEquals("Updated question text.", updatedQuestion.get().getQuestionText());
//    }
//
//    @Test
//    public void testDeleteQuestion() {
//        boolean isDeleted = questionDao.deleteQuestion(testQuestion.getId());
//        assertTrue(isDeleted, "Question should be deleted successfully");
//
//        Optional<Question> deletedQuestion = questionDao.findByQuestionId(testQuestion.getId());
//        assertFalse(deletedQuestion.isPresent(), "Deleted question should not be found");
//    }
//}
