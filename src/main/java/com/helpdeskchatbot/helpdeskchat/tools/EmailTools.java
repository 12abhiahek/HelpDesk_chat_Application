package com.helpdeskchatbot.helpdeskchat.tools;

import com.helpdeskchatbot.helpdeskchat.entity.EmailLog;
import com.helpdeskchatbot.helpdeskchat.repository.EmailLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailTools {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;
    private final Logger logger = LoggerFactory.getLogger(EmailTools.class);

    @Value("${app.email.from:as4290408@gmail.com}")
    private String from;

    public EmailTools(JavaMailSender mailSender, EmailLogRepository emailLogRepository) {
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
    }

    /**
     * Sends an email asynchronously and persists an EmailLog record with status.
     * This method will NOT throw to the caller; failures are recorded so upstream flows (e.g. ticket creation)
     * are not rolled back because of email problems.
     */
    @Async
    public void sendEmailToSupportTeam(String to, String subject, String message, Long ticketId) {
        EmailLog log = EmailLog.builder()
                .recipient(to)
                .subject(subject)
                .body(message)
                .ticketId(ticketId)
                .status(EmailLog.Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // persist initial log
        try {
            emailLogRepository.save(log);
        } catch (Exception e) {
            logger.warn("Unable to persist initial email log for recipient {}: {}", to, e.getMessage());
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);

            log.setStatus(EmailLog.Status.SENT);
            log.setSentAt(LocalDateTime.now());
            emailLogRepository.save(log);
            logger.info("Email sent to {} with subject {}", to, subject);
        } catch (Exception ex) {
            logger.error("Failed to send email to {}: {}", to, ex.getMessage());
            log.setStatus(EmailLog.Status.FAILED);
            log.setErrorMessage(ex.getMessage());
            log.setSentAt(LocalDateTime.now());
            try {
                emailLogRepository.save(log);
            } catch (Exception e) {
                logger.warn("Unable to persist failed email log for recipient {}: {}", to, e.getMessage());
            }
        }
    }

    /**
     * Backwards-compatible helper used by older code that doesn't know about ticketId.
     */
    public void sendEmailToSupportTeam(String to, String subject, String message) {
        sendEmailToSupportTeam(to, subject, message, null);
    }

}
