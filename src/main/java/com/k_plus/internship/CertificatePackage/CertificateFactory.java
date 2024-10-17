package com.k_plus.internship.CertificatePackage;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import javax.sound.sampled.AudioFormat;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.apache.pdfbox.util.Charsets.UTF_8;

@Service
@RequiredArgsConstructor
public class CertificateFactory {

    private final ResourceLoader resourceLoader;

    public ByteArrayInputStream generateCertificate(String userName, String courseName, String result) throws DocumentException, IOException {
        // Создаем документ
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        BaseFont arialFont = BaseFont.createFont(
                "classpath:static/fonts/arial.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
        );
        var bodyFont = new com.itextpdf.text.Font(arialFont, 16, Font.BOLD);
        var headerFont = new com.itextpdf.text.Font(arialFont, 20, Font.BOLD);
        headerFont.setColor(BaseColor.GREEN);
        bodyFont.setColor(BaseColor.DARK_GRAY);

        Paragraph title = new Paragraph("Сертификат о прохождении курса " + courseName, headerFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        document.add(paragraphWrapperWithCenter("Данный сертификат подтверждает, что", bodyFont));
        document.add(paragraphWrapperWithCenter(userName, new com.itextpdf.text.Font(arialFont, 18)));
        document.add(paragraphWrapperWithCenter("успешно завершил курс ", bodyFont));
        document.add(paragraphWrapperWithCenter(courseName, new com.itextpdf.text.Font(arialFont, 18)));
        document.add(paragraphWrapperWithCenter("на платформе правоТворчества ", headerFont));
        document.add(paragraphWrapperWithCenter("Процент правильности прохождения тестов составила: " + result + "%", bodyFont));
        addEmptyParagraphs(document, 10);
        // Добавляем текущую дату внизу
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate = dateFormat.format(new Date());
        Paragraph dateParagraph = new Paragraph("Дата выдачи: " + currentDate, bodyFont);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(dateParagraph);
        // Закрытие документа
//        Image background = Image.getInstance("classpath:static/images/bg_T.png");
//        background.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
//        background.setAbsolutePosition(0, 0);
//
//        PdfContentByte canvas = writer.getDirectContent();
//        canvas.addImage(background, true); // true для использования прозрачности


        document.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private Paragraph paragraphWrapperWithCenter(String text, Font font) {
        Paragraph paragraph = paragraphWrapper(text, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    private Paragraph paragraphWrapper(String text, Font font) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    private void addEmptyParagraphs(Document document,int count) throws DocumentException {
        if (count <= 0) return;

        for (int i = 0; i < count; i++) {
            Paragraph paragraph = new Paragraph(" ");
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        }
    }
}
