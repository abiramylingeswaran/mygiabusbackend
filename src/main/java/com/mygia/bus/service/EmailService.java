package com.mygia.bus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Builds a fresh {@link JavaMailSenderImpl} from SystemSetting values on every send.
 * SMTP credentials are never read from application.properties.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final SettingsService settingsService;

    public EmailService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void send(String to, String subject, String body) {
        String host = settingsService.get(SettingsService.SMTP_HOST).orElse("");
        if (host.isBlank()) {
            log.warn("SMTP host is not configured. Skipping email to {}", to);
            return;
        }
        try {
            JavaMailSenderImpl sender = buildSender();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(settingsService.getOrDefault(SettingsService.SMTP_FROM,
                    settingsService.getOrDefault(SettingsService.SMTP_USERNAME, "noreply@mygia.lk")));
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            sender.send(message);
            log.info("Email sent to {} [{}]", to, subject);
        } catch (Exception ex) {
            log.error("Failed to send email to {}: {}", to, ex.getMessage());
        }
    }

    private JavaMailSenderImpl buildSender() {
        JavaMailSenderImpl mail = new JavaMailSenderImpl();
        mail.setHost(settingsService.getOrDefault(SettingsService.SMTP_HOST, "localhost"));
        mail.setPort(Integer.parseInt(settingsService.getOrDefault(SettingsService.SMTP_PORT, "587")));
        mail.setUsername(settingsService.getOrDefault(SettingsService.SMTP_USERNAME, ""));
        mail.setPassword(settingsService.getOrDefault(SettingsService.SMTP_PASSWORD, ""));

        Properties props = mail.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "8000");
        props.put("mail.smtp.timeout", "8000");
        return mail;
    }
}
