package com.example.mytask.servlet;

import com.example.mytask.connection.HikariCPConnectionPool;
import com.example.mytask.dto.CheckDTO;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.service.CheckService;
import com.example.mytask.service.SimpleCheckService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class RestCheckServlet extends HttpServlet {
    private final CheckService checkService = new SimpleCheckService(new HikariCPConnectionPool());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] productsId = req.getParameterValues("product");
        String[] countItemsId = req.getParameterValues("count");
        String discountCardId = req.getParameter("discount");
        try {
            CheckDTO checkDTO = checkService.getCheckByProductsIdsAndDiscountCardIdI(productsId, countItemsId, discountCardId);
            resp.setStatus(200);
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String jsonString = gson.toJson(checkDTO);
            PrintWriter writer = resp.getWriter();
            writer.write(jsonString);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}
