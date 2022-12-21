package com.example.mytask.dao;

import com.example.mytask.exception.DaoException;
import com.example.mytask.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SimpleProductDao implements ProductDao {
    private static final String SQL_GET_PRODUCTS_BY_ID = "select product_id, product_name, product_price, product_is_promotion from product where ";
    private final Connection connection;

    @Override
    public List<Product> getProductsById(String[] productsId) throws DaoException {
        String generatedSQL = generateSQLByProductsId(productsId);
        try (PreparedStatement preparedStatement = connection.prepareStatement(generatedSQL)) {
            initializePreparedStatement(preparedStatement, productsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractProductsFromResultSet(resultSet);
        } catch (SQLException e) {
            log.error("Cannot get products by id" + Arrays.toString(productsId), e);
            throw new DaoException("Cannot get products by id" + Arrays.toString(productsId), e);
        }
    }

    private void initializePreparedStatement(PreparedStatement preparedStatement, String[] data) throws SQLException {
        for (int i = 0; i < data.length; i++) {
            preparedStatement.setInt(i + 1, Integer.parseInt(data[i]));
        }
    }

    private String generateSQLByProductsId(String[] productsId) {
        StringBuilder generatedSQL = new StringBuilder(SQL_GET_PRODUCTS_BY_ID);
        for (int i = 0; i < productsId.length; i++) {
            if (i == productsId.length - 1) {
                generatedSQL.append("product_id = ? ");
            } else {
                generatedSQL.append("product_id = ? ").append("or ");
            }
        }
        return generatedSQL.toString();
    }

    private List<Product> extractProductsFromResultSet(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product buildedProduct = buildProductByResultSet(resultSet);
            products.add(buildedProduct);
        }
        return products;
    }

    private Product buildProductByResultSet(ResultSet resultSet) throws SQLException {
        return new Product.Builder().
                withId(resultSet.getLong(1)).
                withName(resultSet.getString(2)).
                withPrice(resultSet.getDouble(3)).
                withIsPromotion(resultSet.getBoolean(4)).
                build();
    }
}
