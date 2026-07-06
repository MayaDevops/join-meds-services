package com.joinmeds.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${app.mail.from:JoinMeds <no-reply@joinmeds.com>}")
    private String from;

    /**
     * Sends an HTML mail rendered from a Thymeleaf template.
     * Runs asynchronously so a mail failure never breaks the calling API.
     */
    @Async
    public void sendTemplateMail(String to, String subject, String template, Map<String, Object> variables) {
        if (!mailEnabled) {
            log.info("Mail disabled (app.mail.enabled=false); skipping mail to {}", to);
            return;
        }
        if (to == null || to.isBlank()) {
            log.warn("Skipping mail '{}' — recipient address is empty", subject);
            return;
        }
        try {
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            String html = templateEngine.process(template, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
            log.info("Mail '{}' sent to {}", subject, to);
        } catch (Exception ex) {
            log.error("Failed to send mail '{}' to {}: {}", subject, to, ex.getMessage(), ex);
        }
    }
}