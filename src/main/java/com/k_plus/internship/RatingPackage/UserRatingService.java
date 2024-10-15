package com.k_plus.internship.RatingPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.RatingNotFoundException;
import com.k_plus.internship.CoursePackage.CourseService;
import com.k_plus.internship.TestingPackage.TestingService;
import com.k_plus.internship.UserPackage.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRatingService {

    private final UserRatingRepository userRatingRepository;

    private final TestingService testingService;

    private final UserService userService;

    private final CourseService courseService;

    public UserRating findUserRatingById(UUID userId) {
        return userRatingRepository.findById(userId).orElseThrow(
                () -> new RatingNotFoundException(userId));
    }

    public void addUserRating(UserRating userRating) {
        userRatingRepository.save(userRating);
    }

    @Transactional
    public void addUserRating(UUID userId, UUID courseId, UUID testId,int correctAnswers) {
        UserRating userRating = new UserRating();
        userRating.setId(Generators.timeBasedGenerator().generate());
        userRating.setUser(userService.findUserById(userId));
        userRating.setCorrectCount(correctAnswers);
        userRating.setCourse(courseService.findCourseById(courseId));
        userRatingRepository.save(userRating);
    }

    public long calcUserRatingByCourse(UUID userId, UUID courseId) {
        //long userRating = userRatingRepository.calcUserRatingByCourseId(userId, courseId);
        var ratingsByUser = userRatingRepository.calculateUserRatingByUserIdAndCourseId(userId, courseId);

        return ratingsByUser;
    }

    @Transactional
    public void updateUserRating(UUID userId, UUID courseId, UUID testId, int count) {
        if (!userRatingRepository.existsByUserIdAndTestingId(userId, testId)) {
            addUserRating(userId, courseId, testId, count);
            return;
        }

        var userRating = userRatingRepository.findByUserIdAndTestingId(userId, testId).orElseThrow(() -> new RatingNotFoundException("Rating not found exception" + userId));
        userRating.setCorrectCount(count);
        userRatingRepository.save(userRating);
    }

    public void deleteUserRatingByUserIdAndCourseId(UUID userId, UUID courseId) {
        userRatingRepository.deleteByUserIdAndCourseId(userId, courseId);
    }
}
