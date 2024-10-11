package com.k_plus.internship.SecurityPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.ArticlePackage.Article;
import com.k_plus.internship.ArticlePackage.ArticleService;
import com.k_plus.internship.CommonPackage.CustomExceptions.CourseNotFoundException;
import com.k_plus.internship.CoursePackage.Course;
import com.k_plus.internship.CoursePackage.CourseRepository;
import com.k_plus.internship.OptionPackage.Option;
import com.k_plus.internship.OptionPackage.OptionService;
import com.k_plus.internship.PrivilegePackage.Privilege;
import com.k_plus.internship.PrivilegePackage.PrivilegeRepository;
import com.k_plus.internship.QuestionPackage.Question;
import com.k_plus.internship.QuestionPackage.QuestionService;
import com.k_plus.internship.RolePackage.Role;
import com.k_plus.internship.RolePackage.RoleRepository;
import com.k_plus.internship.TestingPackage.Testing;
import com.k_plus.internship.TestingPackage.TestingService;
import com.k_plus.internship.UserPackage.User;
import com.k_plus.internship.UserPackage.UserRepository;
import com.k_plus.internship.UserPackage.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final QuestionService questionService;
    private final OptionService optionService;

    boolean alreadySetup = false;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    private final ArticleService articleService;

    private final TestingService testingService;

    @Transactional
    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {

        if (alreadySetup){
            return;
        }

        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        var adminRole = roleRepository.findByName("ROLE_ADMIN");
        var userRole = roleRepository.findByName("ROLE_USER");
        if (adminRole.isEmpty() || userRole.isEmpty()) {
            //TODO: add logs!!
            return;
        }

        User user = new User();
        user.setId(Generators.timeBasedEpochGenerator().generate());
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword(passwordEncoder.encode("qweqweqwe"));
        user.setEmail("test@gmail.com");
        user.setRoles(List.of(adminRole.get()));
        user.setEnabled(true);
        if (!userService.existsByEmail(user.getEmail())) {
            userService.saveUser(user);
        }
        String[] names = {
                "Тест",
                "Курс о том как <придумайте сами>",
                "Как стать успешным", "Курс о том как делать курсы",
                "Право", "Костюмы",
                "Общие положения"};
        String[] descriptions = {
                "Это вообще статья а не курс",
                "Это курс про право","про творчество",
                "ну еще и про правоТворчество",
            "Это как будто вообще тест"};
        if (courseRepository.count() < 6) {
            for (int i = 0; i < 5; ++i) {
                createNewTestCourse(names, descriptions);
            }
        }

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        var privilege = privilegeRepository.findByName(name);
        if (privilege.isEmpty()) {
            Privilege newPrivilege = new Privilege();
            newPrivilege.setName(name);
            privilegeRepository.save(newPrivilege);

            return newPrivilege;
        }

        return privilege.get();
    }

    void createNewTestCourse(String[] names, String[] descriptions) {
        Course courseNew = new Course();
        courseNew.setId(Generators.timeBasedEpochGenerator().generate());
        courseRepository.save(courseNew);
        Course course = courseRepository.findById(courseNew.getId()).orElseThrow(
                () -> new CourseNotFoundException("not found course: " + courseNew.getId()));
        course.setName(names[new Random().nextInt(names.length)]);
        course.setDescription(descriptions[new Random().nextInt(descriptions.length)]);
        course.setArticles(createNewArticle(names, descriptions, course));
        course.setTestings(createNewTesting(names, descriptions, course));
        courseRepository.save(course);
    }

    private List<Testing> createNewTesting(String[] names, String[] descriptions, Course course) {
        Testing testing1 = new Testing();
        testing1.setId(Generators.timeBasedEpochGenerator().generate());
        testingService.saveTesting(testing1);
        Testing testing = testingService.findTestingById(testing1.getId());
        testing.setName(names[new Random().nextInt(names.length)]);
        testing.setDescription(descriptions[new Random().nextInt(descriptions.length)]);
        testing.setCourse(course);
        testing.setQuestions(createQuestions(testing, new Random().nextInt(names.length)));
        testing.setDisplayOrder(1);
        testingService.saveTesting(testing);
        return new ArrayList<>(List.of(testing));
    }

    private List<Question> createQuestions(Testing testing, int quantity) {
        String[] questionsVariant = {
                "Тестовый вопрос",
                "Точная дата августовского путча",
                "Какого цвета конституция?", "Интеллектуальное право - это"};
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < quantity; ++i) {
            Question question = new Question();
            question.setId(Generators.timeBasedEpochGenerator().generate());
            questionService.saveQuestion(question);
            Question foundQ = questionService.findQuestionById(question.getId());
            foundQ.setTesting(testing);
            foundQ.setOptions(createOptions(foundQ));
            foundQ.setQuestionText(questionsVariant[new Random().nextInt(questionsVariant.length)]);
            questionService.saveQuestion(foundQ);
            questions.add(foundQ);
        }

        return questions;
    }

    private List<Option> createOptions(Question question) {
        String[] optionVariants = {
                "Да", "Нет", "19.08.1991", "Наверное",
                "Категорически нет!", "зеленый", "Заведомо неверный вариант ответа", "Точно верный вариант ответа"};
        int quantity = optionVariants.length;
        int correctInd = new Random().nextInt(4);
        List<Option> options = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            Option option1 = new Option();
            option1.setId(Generators.timeBasedEpochGenerator().generate());
            optionService.saveOption(option1);
            Option option = optionService.findOptionById(option1.getId());
            option.setQuestion(question);
            option.setOptionText(optionVariants[new Random().nextInt(quantity)]);
            option.setCorrect(i == correctInd);
            options.add(option);
            optionService.saveOption(option);
        }

        return options;
    }

    private List<Article> createNewArticle(String[] names, String[] descriptions, Course course) {
        Article article = new Article();
        article.setId(Generators.timeBasedEpochGenerator().generate());
        article.setTitle(names[new Random().nextInt(names.length)]);
        article.setContent(descriptions[new Random().nextInt(descriptions.length)]
                + descriptions[new Random().nextInt(descriptions.length)]);
        article.setCourse(course);
        article.setDisplayOrder(0);
        articleService.saveArticle(article);
        return new ArrayList<>(List.of(article));
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        var role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(name);
            newRole.setPrivileges(privileges);
            roleRepository.save(newRole);

            return newRole;
        }

        return role.get();
    }
}