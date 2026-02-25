package com.example.demo.accessor;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.core.io.ByteArrayResource;
import java.io.ByteArrayOutputStream;
import com.example.demo.pdf.ProperLetterHeadHandler;

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
    private byte[] generateLetterHeadPdf() {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);


            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new ProperLetterHeadHandler());
            Document document = new Document(pdf);


            document.setMargins(120, 36, 100, 36);


            document.add(new Paragraph(""));

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public void sendPdfMail(String toEmail) {

        try {

            byte[] pdfBytes = generateLetterHeadPdf();

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Letterhead PDF");
            helper.setText("Please find the attached PDF.", true);
            helper.setFrom(SENDER);

            helper.addAttachment("LetterHead.pdf",
                    new ByteArrayResource(pdfBytes));

            javaMailSender.send(message);

            log.info("PDF mail sent to {}", toEmail);

        } catch (Exception e) {
            log.error("Error sending PDF mail", e);
        }
    }
}

