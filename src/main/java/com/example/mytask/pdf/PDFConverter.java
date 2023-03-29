package com.example.mytask.pdf;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.exception.PDFConverterError;

public interface PDFConverter {
     boolean convertToPDFAndSave(CheckDTO checkDTO) throws PDFConverterError;
}
