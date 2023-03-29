package com.example.mytask.pdf;

import java.io.File;

public class SimplePDFConverterFactory implements PDFConverterFactory {
    private static final String FILE_PATH = "C:\\tmp\\pdf\\";

    @Override
    public PDFConverter createPDFConverter() {
        new File(FILE_PATH).mkdirs();
        return new ItextPDFConverter(FILE_PATH);
    }
}
