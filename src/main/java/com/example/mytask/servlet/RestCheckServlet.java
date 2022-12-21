package com.example.mytask.servlet;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.exception.ValidationFailedException;
import com.example.mytask.service.CheckService;
import com.example.mytask.service.CheckServiceFactory;
import com.example.mytask.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class RestCheckServlet extends HttpServlet {
    private static final String PRODUCT_PARAM_NAME_REQUEST = "product";
    private static final String COUNT_PARAM_NAME_REQUEST = "count";
    private static final String DISCOUNT_PARAM_NAME_REQUEST = "discount";
    public static final int OK_STATUS_CODE = 200;
    public static final int BAD_REQUEST_STATUS_CODE = 400;
    public static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;
    private final ServiceFactory serviceFactory = new CheckServiceFactory();
    private final CheckService checkService = serviceFactory.createCheckService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String[] productsId = req.getParameterValues(PRODUCT_PARAM_NAME_REQUEST);
        String[] countItemsId = req.getParameterValues(COUNT_PARAM_NAME_REQUEST);
        String discountCardId = req.getParameter(DISCOUNT_PARAM_NAME_REQUEST);
        proceedWithRequestResponse(req, resp, productsId, countItemsId, discountCardId);
    }

    private void proceedWithRequestResponse(HttpServletRequest req, HttpServletResponse resp, String[] productsId, String[] countItemsId, String discountCardId) {
        CheckDTO checkDTO = null;
        try {
            checkDTO = checkService.getCheckByProductsIdsAndDiscountCardIdI(productsId, countItemsId, discountCardId);
            resp.setStatus(OK_STATUS_CODE);
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String jsonString = gson.toJson(checkDTO);
            PrintWriter writer = resp.getWriter();
            writer.write(jsonString);
        } catch (ValidationFailedException e) {
            resp.setStatus(BAD_REQUEST_STATUS_CODE);
        } catch (ServiceException | IOException e) {
            resp.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }
}
