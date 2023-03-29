package com.example.mytask.pdf;

public class SimplePDFConverterFactory implements PDFConverterFactory{
    private static final String FILE_PATH = "C:\\tmp\\pdf\\";
    @Override
    public PDFConverter createPDFConverter() {
        return new ItextPDFConverter(FILE_PATH);
    }
}
