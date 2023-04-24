package com.example.mytask.servlet;

import com.example.mytask.exception.ServiceException;
import com.example.mytask.model.DiscountCard;
import com.example.mytask.service.DiscountCardService;
import com.example.mytask.service.DiscountCardServiceFactory;
import com.example.mytask.service.SimpleDiscountCardServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RestDiscountCardServlet extends HttpServlet {
    private static final String ID_PARAM_NAME = "id";
    private static final String ALL_PARAM_NAME = "all";
    private static final int OK_STATUS_CODE = 200;
    private static final int NOT_FOUND_STATUS_CODE = 404;
    private static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    private static final String DISCOUNT_CARD_NOT_FOUND_MESSAGE = "Discount card not found";
    private static final String DISCOUNT_VALUE_PARAM_NAME = "discountValue";
    private static final int CREATED_STATUS_CODE = 201;
    private final DiscountCardServiceFactory discountCardServiceFactory = new SimpleDiscountCardServiceFactory();
    private final DiscountCardService discountCardService = discountCardServiceFactory.createDiscountCardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String discountCardId = req.getParameter(ID_PARAM_NAME);
        String allDiscountCards = req.getParameter(ALL_PARAM_NAME);
        proceedDoGet(resp, discountCardId, allDiscountCards);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountValue = req.getParameter(DISCOUNT_VALUE_PARAM_NAME);
        String discountCardId = req.getParameter(ID_PARAM_NAME);
        proceedDoPost(resp, discountValue, discountCardId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountCardId = req.getParameter(ID_PARAM_NAME);
        proceedDoDelete(resp, discountCardId);
    }

    private void proceedDoDelete(HttpServletResponse httpServletResponse, String discountCardId) {
        try {
            if (discountCardService.deleteById(discountCardId)) {
                httpServletResponse.setStatus(OK_STATUS_CODE);
            } else {
                httpServletResponse.setStatus(NOT_FOUND_STATUS_CODE);
            }
        } catch (ServiceException e) {
            log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
            httpServletResponse.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }

    private void proceedDoPost(HttpServletResponse httpServletResponse, String discountValue, String discountCardId) {
        if (discountValue == null) {
            httpServletResponse.setStatus(NOT_FOUND_STATUS_CODE);
            return;
        }
        try (PrintWriter writer = httpServletResponse.getWriter()) {
            if (discountCardId != null) {
                DiscountCard createdDiscountCard = discountCardService.updateById(discountCardId, Double.parseDouble(discountValue));
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                String jsonString = gson.toJson(createdDiscountCard, DiscountCard.class);
                writer.write(jsonString);
                httpServletResponse.setStatus(OK_STATUS_CODE);
            } else {
                DiscountCard createdDiscountCard = discountCardService.create(Double.parseDouble(discountValue));
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                String jsonString = gson.toJson(createdDiscountCard, DiscountCard.class);
                writer.write(jsonString);
                httpServletResponse.setStatus(CREATED_STATUS_CODE);
            }
        } catch (ServiceException | IOException e) {
            log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
            httpServletResponse.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }

    private void proceedDoGet(HttpServletResponse httpServletResponse, String discountCardId, String allDiscountCards) {
        try (PrintWriter writer = httpServletResponse.getWriter()) {
            if (discountCardId != null) {
                Optional<DiscountCard> discountCard = discountCardService.getCardById(discountCardId);
                if (discountCard.isPresent()) {
                    DiscountCard discountCardFromOptional = discountCard.get();
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setPrettyPrinting();
                    Gson gson = gsonBuilder.create();
                    String jsonString = gson.toJson(discountCardFromOptional, DiscountCard.class);
                    writer.write(jsonString);
                    httpServletResponse.setStatus(OK_STATUS_CODE);
                } else {
                    log.error(DISCOUNT_CARD_NOT_FOUND_MESSAGE);
                    httpServletResponse.setStatus(NOT_FOUND_STATUS_CODE);
                }
            } else if (allDiscountCards != null) {
                List<DiscountCard> discountCards = discountCardService.findAll();
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                String jsonString = gson.toJson(discountCards);
                writer.write(jsonString);
                httpServletResponse.setStatus(OK_STATUS_CODE);
            }
        } catch (ServiceException | IOException e) {
            log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
            httpServletResponse.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }
}
