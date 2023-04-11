package com.example.mytask.servlet;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.exception.PDFConverterError;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.exception.ValidationFailedException;
import com.example.mytask.pdf.PDFConverter;
import com.example.mytask.pdf.PDFConverterFactory;
import com.example.mytask.pdf.SimplePDFConverterFactory;
import com.example.mytask.service.CheckService;
import com.example.mytask.service.SimpleCheckServiceFactory;
import com.example.mytask.service.CheckServiceFactory;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class RestCheckServlet extends HttpServlet {
    private static final int OK_STATUS_CODE = 200;
    private static final int BAD_REQUEST_STATUS_CODE = 400;
    private static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed ";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    private static final String CANNOT_CONVERT_TO_PDF_MESSAGE = "Cannot convert to pdf";
    private static final String PRODUCT_PARAM_NAME_REQUEST = "product";
    private static final String COUNT_PARAM_NAME_REQUEST = "count";
    private static final String DISCOUNT_PARAM_NAME_REQUEST = "discount";
    private final CheckServiceFactory serviceFactory = new SimpleCheckServiceFactory();
    private final PDFConverterFactory pdfConverterFactory = new SimplePDFConverterFactory();
    private final CheckService checkService = serviceFactory.createCheckService();
    private final PDFConverter pdfConverter = pdfConverterFactory.createPDFConverter();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String[] productsId = req.getParameterValues(PRODUCT_PARAM_NAME_REQUEST);
        String[] countItemsId = req.getParameterValues(COUNT_PARAM_NAME_REQUEST);
        String discountCardId = req.getParameter(DISCOUNT_PARAM_NAME_REQUEST);
        proceedWithResponse(resp, productsId, countItemsId, discountCardId);
    }

    private void proceedWithResponse(HttpServletResponse resp, String[] productsId, String[] countItemsId, String discountCardId) {
        CheckDTO checkDTO = null;
        try {
            checkDTO = checkService.getCheckByProductsIdsAndDiscountCardId(productsId, countItemsId, discountCardId);
            long pdfFileId = pdfConverter.convertToPDFAndSave(checkDTO);
            readFileFromFileStorageAndAddToResponse(resp, SimplePDFConverterFactory.FILE_PATH, pdfFileId);
            resp.setStatus(OK_STATUS_CODE);
        } catch (ValidationFailedException e) {
            log.error(VALIDATION_FAILED_MESSAGE + Arrays.toString(productsId) + Arrays.toString(countItemsId) + discountCardId, e);
            resp.setStatus(BAD_REQUEST_STATUS_CODE);
        } catch (ServiceException | IOException e) {
            log.error(INTERNAL_SERVER_ERROR_MESSAGE + Arrays.toString(productsId) + Arrays.toString(countItemsId) + discountCardId, e);
            resp.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        } catch (PDFConverterError e) {
            log.error(CANNOT_CONVERT_TO_PDF_MESSAGE, e);
            resp.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }

    private void readFileFromFileStorageAndAddToResponse(HttpServletResponse response, String filePath, long fileId) throws IOException {
        response.setContentType("application/pdf");
        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        FileInputStream fis = new FileInputStream(filePath + fileId + ".pdf");
        int c;
        while ((c = fis.read()) != -1) bos.write(c);
        bos.flush();
        bos.close();
    }
}
