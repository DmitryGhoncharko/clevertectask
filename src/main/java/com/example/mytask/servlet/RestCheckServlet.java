package com.example.mytask.servlet;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.exception.ValidationFailedException;
import com.example.mytask.pdf.PDFConverter;
import com.example.mytask.pdf.PDFConverterFactory;
import com.example.mytask.pdf.SimplePDFConverterFactory;
import com.example.mytask.service.CheckService;
import com.example.mytask.service.CheckServiceFactory;
import com.example.mytask.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@Slf4j
public class RestCheckServlet extends HttpServlet {
    public static final int OK_STATUS_CODE = 200;
    public static final int BAD_REQUEST_STATUS_CODE = 400;
    public static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;
    public static final String VALIDATION_FAILED_MESSAGE = "Validation failed ";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    private static final String PRODUCT_PARAM_NAME_REQUEST = "product";
    private static final String COUNT_PARAM_NAME_REQUEST = "count";
    private static final String DISCOUNT_PARAM_NAME_REQUEST = "discount";
    private final ServiceFactory serviceFactory = new CheckServiceFactory();
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
            pdfConverter.convertToPDFAndSave(checkDTO);
            resp.setStatus(OK_STATUS_CODE);
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String jsonString = gson.toJson(checkDTO);
            PrintWriter writer = resp.getWriter();
            writer.write(jsonString);
        } catch (ValidationFailedException e) {
            log.error(VALIDATION_FAILED_MESSAGE + Arrays.toString(productsId) + Arrays.toString(countItemsId) + discountCardId);
            resp.setStatus(BAD_REQUEST_STATUS_CODE);
        } catch (ServiceException | IOException e) {
            log.error(INTERNAL_SERVER_ERROR_MESSAGE + Arrays.toString(productsId) + Arrays.toString(countItemsId) + discountCardId);
            resp.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }
}
