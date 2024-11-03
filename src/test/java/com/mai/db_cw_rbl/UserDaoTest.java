//package com.mai.db_cw_rbl;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import com.mai.db_cw_rbl.UserPackage.Dao.UserDaoImpl;
//import com.mai.db_cw_rbl.UserPackage.User;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.*;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.transaction.annotation.Transactional;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional
//@Sql(scripts = "/data.sql") // Assuming you have a schema.sql in src/test/resources
//public class UserDaoTest {
//
//    @Autowired
//    private UserDaoImpl userDao;
//
//    private User testUser;
//
//    @BeforeEach
//    public void setUp() {
//        // Set up a test user
//        testUser = new User(
//                UUID.randomUUID(),
//                "John",
//                "Doe",
//                "john.doe@example.com",
//                "password",
//                true,
//                false,
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//        userDao.save(testUser);
//    }
//
//    @Test
//    public void testFindById() {
//        Optional<User> foundUser = userDao.findById(testUser.getId());
//        assertTrue(foundUser.isPresent());
//        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
//    }
//
//    @Test
//    public void testFindByEmail() {
//        Optional<User> foundUser = userDao.findByEmail(testUser.getEmail());
//        assertTrue(foundUser.isPresent());
//        assertEquals(testUser.getId(), foundUser.get().getId());
//    }
//
//    @Test
//    public void testExistsByEmail() {
//        boolean exists = userDao.existsByEmail(testUser.getEmail());
//        assertTrue(exists);
//    }
//
//    @Test
//    public void testSave() {
//        User newUser = new User(
//                UUID.randomUUID(),
//                "Jane",
//                "Smith",
//                "jane.smith@example.com",
//                "password123",
//                true,
//                false,
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//        userDao.save(newUser);
//
//        Optional<User> foundUser = userDao.findByEmail("jane.smith@example.com");
//        assertTrue(foundUser.isPresent());
//        assertEquals("Jane", foundUser.get().getFirstName());
//    }
//
//    @Test
//    public void testDeleteByEmail() {
//        boolean deleted = userDao.deleteByEmail(testUser.getEmail());
//        assertTrue(deleted);
//
//        Optional<User> foundUser = userDao.findByEmail(testUser.getEmail());
//        assertFalse(foundUser.isPresent());
//    }
//
//    @Test
//    public void testDeleteById() {
//        boolean deleted = userDao.deleteById(testUser.getId());
//        assertTrue(deleted);
//
//        Optional<User> foundUser = userDao.findById(testUser.getId());
//        assertFalse(foundUser.isPresent());
//    }
//
//    @Test
//    public void testFindAll() {
//        List<User> users = userDao.findAll();
//        assertFalse(users.isEmpty());
//        assertEquals(1, users.size()); // Only the setup user should exist
//        assertEquals(testUser.getEmail(), users.get(0).getEmail());
//    }
//}