package com.example.newsappwithauth.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EmailHandler {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendMail(String toEmail, String otp, String subject, String emailContent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(emailContent);
            javaMailSender.send(message);
        } catch (MailAuthenticationException e) {
//            log.error("Mail Authentication failed for email: {}", toEmail, e);
//            log.error("Authentication failed with error: {}", e.getMessage());
            throw new RuntimeException("Mail authentication failed", e);
        } catch (Exception e) {
//            log.error("Error occurred while sending email", e);
            throw new RuntimeException("Error occurred while sending email", e);
        }
    }
}
