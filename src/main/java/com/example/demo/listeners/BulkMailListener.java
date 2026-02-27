package com.example.demo.listeners;

import com.example.demo.events.BulkMailEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BulkMailListener {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String SENDER = "bulkmail1@catchyou.online";

    @Async
    @EventListener
    public void handleBulkMail(BulkMailEvent event) {

        event.getEmails()
                .parallelStream()
                .forEach(this::sendMail);
    }

    private void sendMail(String email) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Bulk Mail");
            helper.setText("This is bulk mail.", true);
            helper.setFrom(SENDER);

            javaMailSender.send(message);

            log.info("Sent to {}", email);

        } catch (Exception e) {
            log.error("Error sending to {}", email, e);
        }
    }
}