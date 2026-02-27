package com.example.demo.accessor;

import com.example.demo.events.BulkMailEvent;
import com.example.demo.pdf.ProperLetterHeadHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Service
public class EmailAccessor {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ApplicationEventPublisher publisher;

    private static final String SENDER = "shreya.settyvn@gmail.com";
    private static final String OTP_TEMPLATE = "hello";


    public void sendEmail(String toEmail) throws MessagingException {

        Context context = new Context();
        String htmlContent = templateEngine.process(OTP_TEMPLATE, context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Single Mail");
        helper.setText(htmlContent, true);
        helper.setFrom(SENDER);

        javaMailSender.send(message);
    }


    public void sendBulkEmails(List<String> emails) {
        publisher.publishEvent(new BulkMailEvent(emails));
    }


    private byte[] generateLetterHeadPdf() {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);

            byte[] logoBytes = getLogoFromS3();

            pdf.addEventHandler(
                    PdfDocumentEvent.END_PAGE,
                    new ProperLetterHeadHandler(logoBytes)
            );


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
            System.out.println("MAIL SENT SUCCESSFULLY TO SMTP SERVER");

            log.info("PDF mail sent to {}", toEmail);

        } catch (Exception e) {
            log.error("Error sending PDF mail", e);
        }
    }

    public byte[] getLogoFromS3() {

        try {

            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket("rizzle-shreya")
                    .key("logo.png")
                    .build();

            return s3Client.getObject(request).readAllBytes();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch logo from S3", e);
        }
    }
}