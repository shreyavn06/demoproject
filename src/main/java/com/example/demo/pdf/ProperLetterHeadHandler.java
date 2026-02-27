package com.example.demo.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class ProperLetterHeadHandler implements IEventHandler {

    private final byte[] logoBytes;

    public ProperLetterHeadHandler(byte[] logoBytes) {
        this.logoBytes = logoBytes;
    }

    @Override
    public void handleEvent(Event event) {

        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();

        PdfCanvas pdfCanvas = new PdfCanvas(
                page.newContentStreamBefore(),
                page.getResources(),
                pdfDoc
        );

        Canvas canvas = new Canvas(pdfCanvas, pageSize);

        float width = pageSize.getWidth();
        float top = pageSize.getTop();
        float bottom = pageSize.getBottom();

        // 🔹 Add Logo (Left side of header)
        if (logoBytes != null) {
            Image logo = new Image(ImageDataFactory.create(logoBytes));
            logo.scaleToFit(60, 60);
            logo.setFixedPosition(40, top - 80);
            canvas.add(logo);
        }

        // 🔹 Company Name
        canvas.showTextAligned(
                new Paragraph("RIZZLE TECHNOLOGY PRIVATE LIMITED")
                        .setBold()
                        .setFontSize(16),
                width / 2 + 20,
                top - 40,
                TextAlignment.CENTER
        );

        // 🔹 CIN
        canvas.showTextAligned(
                new Paragraph("CIN No: U72900AP2019PTC110174"),
                width / 2 + 20,
                top - 60,
                TextAlignment.CENTER
        );

        // 🔹 Contact
        canvas.showTextAligned(
                new Paragraph("Contact No: +91-9087817126 | tech@rizzle.in"),
                width / 2 + 20,
                top - 75,
                TextAlignment.CENTER
        );

        // 🔹 Line below header
        pdfCanvas.moveTo(40, top - 95);
        pdfCanvas.lineTo(width - 40, top - 95);
        pdfCanvas.stroke();

        // 🔹 Line above footer
        pdfCanvas.moveTo(40, bottom + 60);
        pdfCanvas.lineTo(width - 40, bottom + 60);
        pdfCanvas.stroke();

        // 🔹 Footer Text
        canvas.showTextAligned(
                new Paragraph("11th Floor, Innov8 Prestige Tech Platina,"),
                width / 2,
                bottom + 40,
                TextAlignment.CENTER
        );

        canvas.showTextAligned(
                new Paragraph("No 32/2, 34/1 Kadabisanahalli, Bengaluru - 560087"),
                width / 2,
                bottom + 25,
                TextAlignment.CENTER
        );

        canvas.close();
    }
}