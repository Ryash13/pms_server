package com.jfs.pms.utility;

import com.jfs.pms.constants.enums.EmailTemplates;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, EmailTemplates templates, Map<String, Object> contextVariables) throws MessagingException {
        var templateName = templates.value();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED, UTF_8.name());

        Context context = new Context();
        context.setVariables(contextVariables);

        String template = templateEngine.process(templateName, context);

        helper.setTo(to);
        helper.setFrom("yash.11717121@gmail.com");
        helper.setSubject(subject);
        helper.setText(template, true);
        log.info("Sending email to {}, with template {}", to, templateName);
        mailSender.send(message);
    }
}
