package com.example.demo.accessor;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class EmailAccessor {

    @Autowired
    private ExecutorService mailExecutor;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private static final String SENDER = "bulkmail1@catchyou.online";
    private static final String OTP_TEMPLATE = "hello";

    // Existing single email method
    public void sendEmail(String toEmail) throws MessagingException {

        Context context = new Context();
        String htmlContent = templateEngine.process(OTP_TEMPLATE, context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("A sample text");
        helper.setText(htmlContent, true);
        helper.setFrom(SENDER);

        javaMailSender.send(message);
    }



    public void sendBulkEmails(List<String> emails) {
        AtomicInteger counter = new AtomicInteger(1);

        emails.forEach(email ->
                mailExecutor.submit(() ->
                        sendSingleEmail(email, counter.getAndIncrement())
                )
        );
    }

    private void sendSingleEmail(String email, int count) {

        try {

            Context context = new Context();
            context.setVariable("count", count);

            String htmlContent = templateEngine.process(OTP_TEMPLATE, context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Email " + count + " sent");
            helper.setText(htmlContent, true);
            helper.setFrom(SENDER);

            javaMailSender.send(message);

            log.info("Email sent to: {}", email);

        } catch (Exception e) {
            log.error("Error sending to: {}", email, e);
        }
    }
}

