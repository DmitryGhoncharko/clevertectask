package com.example.mytask.pdf;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.dto.ProductDTO;
import com.example.mytask.exception.PDFConverterError;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
    public void convertToPDFAndSave(CheckDTO checkDTO) throws PDFConverterError {
        Document document = new Document();
        if (checkDTO == null) {
            throw new PDFConverterError("Cannot create check, checkDTO is null");
        }
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath + checkId.incrementAndGet() + ".pdf"));
            document.open();
            addContent(document, checkDTO);
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
        createTable(subCatPart);
        document.add(catPart);
        document.add(catPart);

    }

    private void createList(Section subCatPart, CheckDTO checkDTO) {
        List list = new List(true, false, 10);
        for (ProductDTO productDTO : checkDTO.getProducts()) {
            list.add(new ListItem("Product name: " + productDTO.getProduct().getName() + " count: " + productDTO.getCount() + " final price: " + productDTO.getFinalPrise()));
        }
        if (checkDTO.getDiscountCard() != null) {
            list.add(new ListItem("Discount: " + checkDTO.getDiscountCard().getDiscount()));
            list.add(new ListItem("Total price: " + checkDTO.getTotalPrice()));
        }
        subCatPart.add(list);
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void createTable(Section subCatPart) throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }
}
