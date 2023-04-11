package com.example.mytask.servlet;

import com.example.mytask.exception.ServiceException;
import com.example.mytask.model.Product;
import com.example.mytask.service.ProductService;
import com.example.mytask.service.ProductServiceFactory;
import com.example.mytask.service.SimpleProductServiceFactory;
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
public class RestProductServlet extends HttpServlet {
    private static final String ID_PARAM_NAME = "id";
    private static final String ALL_PARAM_NAME = "all";
    private static final int OK_STATUS_CODE = 200;
    private static final int NOT_FOUND_STATUS_CODE = 404;
    private static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    private static final String DISCOUNT_CARD_NOT_FOUND_MESSAGE = "Discount card not found";
    private static final String DISCOUNT_VALUE_PARAM_NAME = "discountValue";
    private static final int CREATED_STATUS_CODE = 201;
    private static final String PRICE_PARAM_NAME = "price";
    private static final String PROMOTION_PARAM_NAME = "promotion";
    private final ProductServiceFactory productServiceFactory = new SimpleProductServiceFactory();
    private final ProductService productService = productServiceFactory.createService();

    public static String getPriceParamName() {
        return PRICE_PARAM_NAME;
    }

    public static String getPromotionParamName() {
        return PROMOTION_PARAM_NAME;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String productId = req.getParameter(ID_PARAM_NAME);
        String allProducts = req.getParameter(ALL_PARAM_NAME);
        proceedDoGet(resp, productId, allProducts);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productName = req.getParameter(DISCOUNT_VALUE_PARAM_NAME);
        String productId = req.getParameter(ID_PARAM_NAME);
        String productPrice = req.getParameter(getPriceParamName());
        String isPromotion = req.getParameter(getPromotionParamName());
        proceedDoPost(resp, productId, productName, productPrice, isPromotion);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter(ID_PARAM_NAME);
        proceedDoDelete(resp, productId);
    }

    private void proceedDoDelete(HttpServletResponse httpServletResponse, String productId) {
        try {
            if (productService.deleteById(productId)) {
                httpServletResponse.setStatus(OK_STATUS_CODE);
            } else {
                httpServletResponse.setStatus(NOT_FOUND_STATUS_CODE);
            }
        } catch (ServiceException e) {
            log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
            httpServletResponse.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }

    private void proceedDoPost(HttpServletResponse httpServletResponse, String productId, String productName, String productPrice, String productIsPromotion) {
        if (productName == null || productPrice == null || productIsPromotion == null) {
            httpServletResponse.setStatus(NOT_FOUND_STATUS_CODE);
            return;
        }
        try (PrintWriter writer = httpServletResponse.getWriter()) {
            if (productId != null) {
                Product product = productService.updateById(productId, productName, Double.parseDouble(productPrice), Boolean.parseBoolean(productIsPromotion));
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                String jsonString = gson.toJson(product, Product.class);
                writer.write(jsonString);
                httpServletResponse.setStatus(OK_STATUS_CODE);
            } else {
                Product product = productService.create(productName, Double.parseDouble(productPrice), Boolean.parseBoolean(productIsPromotion));
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                String jsonString = gson.toJson(product, Product.class);
                writer.write(jsonString);
                httpServletResponse.setStatus(CREATED_STATUS_CODE);
            }
        } catch (ServiceException | IOException e) {
            log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
            httpServletResponse.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }

    private void proceedDoGet(HttpServletResponse httpServletResponse, String productId, String allProducts) {
        try (PrintWriter writer = httpServletResponse.getWriter()) {
            if (productId != null) {
                Optional<Product> product = productService.getProductById(productId);
                if (product.isPresent()) {
                    Product productFromOptional = product.get();
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setPrettyPrinting();
                    Gson gson = gsonBuilder.create();
                    String jsonString = gson.toJson(productFromOptional, Product.class);
                    writer.write(jsonString);
                    httpServletResponse.setStatus(OK_STATUS_CODE);
                } else {
                    log.error(DISCOUNT_CARD_NOT_FOUND_MESSAGE);
                    httpServletResponse.setStatus(NOT_FOUND_STATUS_CODE);
                }
            } else if (allProducts != null) {
                List<Product> products = productService.findAll();
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                String jsonString = gson.toJson(products);
                writer.write(jsonString);
                httpServletResponse.setStatus(OK_STATUS_CODE);
            }
        } catch (ServiceException | IOException e) {
            log.error(INTERNAL_SERVER_ERROR_MESSAGE, e);
            httpServletResponse.setStatus(INTERNAL_SERVER_ERROR_STATUS_CODE);
        }
    }
}
