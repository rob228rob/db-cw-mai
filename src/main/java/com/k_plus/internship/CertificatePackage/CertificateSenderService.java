package com.k_plus.internship.CertificatePackage;

import com.itextpdf.text.DocumentException;
import com.k_plus.internship.CoursePackage.Course;
import com.k_plus.internship.CoursePackage.CourseService;
import com.k_plus.internship.EmailSender.EmailSenderService;
import com.k_plus.internship.StatisticsPackage.StatResponseDto;
import com.k_plus.internship.StatisticsPackage.StatisticsService;
import com.k_plus.internship.UserPackage.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateSenderService {

    private final CertificateFactory certificateFactory;

    private final EmailSenderService emailSenderService;

    private final StatisticsService statisticsService;

    private final CourseService courseService;

    private final CertificateService certificateService;


    @Scheduled(fixedRate = 300000)
    public void checkUserProgressAndSendCertificateIfRequired() {

        var certificateCount = courseService.findAll()
                .peek(course ->
                        courseService.getUsersByCourseId(course.getId())
                        .forEach(user -> sendCertificateIfRequired(user, course)))
                .toList();
        log.info("SCHEDULING ENDED");

    }

    private void sendCertificateIfRequired(User user, Course course) {
        var stats = statisticsService.findStatsRankByCourse(course.getId(), user.getId());
        if (stats.isCourseCompleted() && !certificateAlreadySent(user, course)) {
            sendAndSaveCertificate(user, course, stats);
        }
    }

    private boolean certificateAlreadySent(User user, Course course) {
        return certificateService.isAlreadySent(user, course);
    }

    private void sendAndSaveCertificate(User user, Course course, StatResponseDto stats) {
        var userFullName = user.getFirstName() + " " + user.getLastName();
        var subject = "Поздравляем с прохождением курса " + course.getName();
        try (var pdfCertificate = certificateFactory.generateCertificate(
                userFullName,
                course.getName(),
                String.valueOf(stats.getPercentageCorrect()))
        ) {
            emailSenderService.sendCertificate(
                    user.getEmail(),
                    subject,
                    userFullName,
                    course.getName(),
                    pdfCertificate);
            certificateService.saveCertificate(pdfCertificate, course, user.getEmail(), subject, true);
            log.info("\ncertificate SENT to: {}\n", user.getEmail());
        } catch (IOException e) {
            log.error("Error creating pdf: {}", e.getMessage());
        } catch (MessagingException | DocumentException e) {
            log.error("Error sending pdf: {}", e.getMessage());
        }
    }

}
