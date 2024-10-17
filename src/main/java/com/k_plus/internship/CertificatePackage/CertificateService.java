package com.k_plus.internship.CertificatePackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.EntityNotFoundException;
import com.k_plus.internship.CoursePackage.Course;
import com.k_plus.internship.UserPackage.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final CertificateRepository certificateRepository;

    private final FilePDFRepository filePDFRepository;

    @Transactional
    public void saveCertificate(ByteArrayInputStream pdf, Course course, String email, String subject, boolean sent) {
        Certificate certificate = new Certificate();
        certificate.setId(Generators.timeBasedEpochGenerator().generate());
        certificate.setSent(sent);
        certificate.setSubject(subject);
        certificate.setUserEmail(email);
        certificateRepository.save(certificate);

        var found = certificateRepository.findById(certificate.getId()).orElseThrow(
                () -> new EntityNotFoundException("Certificate not found with id: " + certificate.getId()));

        found.setPdf(savePdf(pdf, found));
        found.setCourse(course);

        certificateRepository.save(found);
    }

    private FilePDF savePdf(ByteArrayInputStream pdf, Certificate certificate) {
        FilePDF filePDF = new FilePDF();
        filePDF.setId(Generators.timeBasedEpochGenerator().generate());

        filePDFRepository.save(filePDF);

        FilePDF found = filePDFRepository.findById(filePDF.getId()).orElseThrow(
                () -> new EntityNotFoundException("PDF not found with id: " + filePDF.getId()));

        found.setFile(pdf.readAllBytes());
        found.setCertificate(certificate);

        return filePDFRepository.save(found);
    }

    public boolean isAlreadySent(User user, Course course) {
        return certificateRepository.isAlreadySent(user.getEmail(), course.getId()).orElse(false);
    }
}
