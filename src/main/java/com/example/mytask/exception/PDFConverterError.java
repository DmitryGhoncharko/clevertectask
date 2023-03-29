package com.example.mytask.exception;

public class PDFConverterError extends Error{
    public PDFConverterError() {
    }

    public PDFConverterError(String message) {
        super(message);
    }

    public PDFConverterError(String message, Throwable cause) {
        super(message, cause);
    }
}
