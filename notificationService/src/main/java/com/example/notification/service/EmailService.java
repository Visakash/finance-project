package com.example.notification.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private static final String FROM = "akashvispute37@gmail.com";

    public String sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(FROM);
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            mailSender.send(mail);
            log.info("Email sent → {} | {}", to, subject);
            return "Email sent successfully.";
        } catch (Exception e) {
            log.error("Email failed → {}: {}", to, e.getMessage());
            return "Email delivery failed.";
        }
    }

    public String sendEmail(String to, String msg) {
        return sendEmail(to, resolveSubject(msg), msg);
    }

    private String resolveSubject(String msg) {
        if (msg == null) return "LoanCore Notification";
        String m = msg.toLowerCase();
        if (m.contains("otp"))                                  return "LoanCore — Your OTP";
        if (m.contains("welcome") || m.contains("registered")) return "Welcome to LoanCore!";
        return "LoanCore Notification";
    }
}