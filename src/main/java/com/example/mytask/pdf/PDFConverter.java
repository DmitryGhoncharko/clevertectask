package com.example.mytask.pdf;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.exception.PDFConverterError;

public interface PDFConverter {
    public void convertToPDFAndSave(CheckDTO checkDTO) throws PDFConverterError;
}
