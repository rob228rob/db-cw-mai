package com.mai.db_cw_rbl.EmailSender;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    public void sendCertificate(
            String to,
            String subject,
            String fullUserName,
            String courseName,
            ByteArrayInputStream certificateStream) throws MessagingException, IOException {
        if (fullUserName.isEmpty() || courseName.isEmpty()) {
            log.error("Full username or course name is empty: name: {}, course: {}", fullUserName, courseName);
            throw new IllegalArgumentException("Full username or course name is empty");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);

        var htmlContent = """
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Привет, на связи правовая платформа поддержки.
                        Поздравляем с успешным завершением курса!</title>
                    <style>
                        body {
                            font-family: 'Arial', sans-serif;
                            background-color: #f9f9f9;
                            color: #333;
                            padding: 20px;
                            text-align: center;
                        }
                        .container {
                            background-color: #fff;
                            border-radius: 8px;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                            padding: 40px;
                            max-width: 600px;
                            margin: 0 auto;
                        }
                        h1 {
                            color: #4CAF50;
                            font-size: 28px;
                            margin-bottom: 20px;
                        }
                        p {
                            font-size: 18px;
                            line-height: 1.6;
                            margin-bottom: 20px;
                        }
                        .button {
                            display: inline-block;
                            background-color: #4CAF50;
                            color: #fff;
                            padding: 10px 20px;
                            border-radius: 5px;
                            text-decoration: none;
                            font-size: 18px;
                        }
                        .button:hover {
                            background-color: #45a049;
                        }
                        footer {
                            margin-top: 30px;
                            font-size: 14px;
                            color: #777;
                        }
                    </style>
                </head>
                <body>
                <div class="container">
                    <h1>Поздравляем, {{userName}}!</h1>
                    <p>Вы успешно завершили курс <strong>{{courseName}}</strong>.</p>
                    <p>Мы рады сообщить, что ваш сертификат о прохождении курса доступен. Он прилагается к этому письму.</p>
                </div>
                <footer>
                    Это письмо было отправлено автоматически, не нужно на него отвечать.
                    <h1>правовая поддержка</h1>
                </footer>
                </body>
                </html>"""
                .replace("{{userName}}", fullUserName)
                .replace("{{courseName}}", courseName);

        helper.setText(htmlContent, true);

        var userNameForFilename = fullUserName.trim().replace(' ', '_');
        helper.addAttachment(
                "certificate_"
                        + courseName.trim().replace(' ', '_')
                        + "_" + userNameForFilename + ".pdf",
                new ByteArrayResource(certificateStream.readAllBytes()));

        mailSender.send(message);
    }

    public void sendSimpleHtmlMail(String to, String subject, String text, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error sending mail: {}",e.getMessage());
        }
    }
}