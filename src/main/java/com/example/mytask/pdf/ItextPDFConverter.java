package com.example.mytask.pdf;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.dto.ProductDTO;
import com.example.mytask.exception.PDFConverterError;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
public class ItextPDFConverter implements PDFConverter {
    private static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final AtomicLong checkId = new AtomicLong(0);
    private final String filePath;

    @Override
    public boolean convertToPDFAndSave(CheckDTO checkDTO) throws PDFConverterError {
        Document document = new Document();
        if (checkDTO == null) {
            throw new PDFConverterError("Cannot create check, checkDTO is null");
        }
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath + checkId.incrementAndGet() + ".pdf"));
            document.open();
            addContent(document, checkDTO);
            document.close();
            return true;
        } catch (Exception e) {
            log.error("Cannot create check", e);
            throw new PDFConverterError("Cannot create check", e);
        }

    }

    private void addContent(Document document, CheckDTO checkDTO) throws DocumentException {
        Anchor anchor = new Anchor("CHECK", catFont);
        anchor.setName("CHECK");
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);
        Paragraph subPara = new Paragraph("", subFont);
        Section subCatPart = catPart.addSection(subPara);
        createList(subCatPart, checkDTO);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);
        document.add(catPart);

    }

    private void createList(Section subCatPart, CheckDTO checkDTO) {
        List list = new List(true, false, 10);
        for (ProductDTO productDTO : checkDTO.getProducts()) {
            list.add(new ListItem("Product name: " + productDTO.getProduct().getName() + " count: " + productDTO.getCount() + " final price: " + productDTO.getFinalPrise()));
        }
        if (checkDTO.getDiscountCard() != null) {
            list.add(new ListItem("Discount: " + checkDTO.getDiscountCard().getDiscount()));
        }
        list.add(new ListItem("Total price: " + checkDTO.getTotalPrice()));
        subCatPart.add(list);
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
