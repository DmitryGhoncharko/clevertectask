package com.example.mytask.dao;

import com.example.mytask.connection.ConnectionPool;
import com.example.mytask.exception.DaoException;
import com.example.mytask.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PostgresqlProductDao implements ProductDao {
    private static final String SQL_GET_PRODUCTS_BY_ID = "select product_id, product_name, product_price, product_is_promotion from product where ";
    private static final String CANNOT_GET_PRODUCTS_BY_ID_MESSAGE = "Cannot get products by id";
    private static final String PRODUCT_ID_GENERATED_FOR_SQL_PART = "product_id = ? ";

    private static final String SQL_GET_ALL_PRODUCTS = "select product_id, product_name, product_price, product_is_promotion from product where product_id = ?";
    private static final String SQL_UPDATE_PRODUCT_BY_ID = "update product set product_name = ?, product_price = ?, product_is_promotion = ? where product_id = ?";
    private static final String OR_OPERATOR_FOR_SQL = "or ";
    private static final String SQL_DELETE_PRODUCT_BY_ID = "delete from product where product_id = ?";

    private static final String SQL_CREATE_PRODUCT = "insert into product(product_name,product_price,product_is_promotion) values(?,?,?)";
    private static final String CANNOT_FOUND_PRODUCT_BY_ID_MESSAGE = "Cannot found product by id, id = ";
    private static final String CANNOT_FIND_ALL_PRODUCTS_MESSAGE = "Cannot find all products";
    private static final String CANNOT_UPDATE_PRODUCT_BY_ID_MESSAGE = "Cannot update product by id";
    private static final String CANNOT_DETELETE_PRODUCT_BY_ID_MESSAGE = "Cannot detelete product by id , id";
    private static final String CANNOT_CREATE_PRODUCT_MESSAGE = "Cannot create product";
    private final ConnectionPool connectionPool;

    public static String getCannotFoundProductByIdMessage() {
        return CANNOT_FOUND_PRODUCT_BY_ID_MESSAGE;
    }

    public static String getCannotFindAllProductsMessage() {
        return CANNOT_FIND_ALL_PRODUCTS_MESSAGE;
    }

    public static String getCannotUpdateProductByIdMessage() {
        return CANNOT_UPDATE_PRODUCT_BY_ID_MESSAGE;
    }

    public static String getCannotDeteleteProductByIdMessage() {
        return CANNOT_DETELETE_PRODUCT_BY_ID_MESSAGE;
    }

    public static String getCannotCreateProductMessage() {
        return CANNOT_CREATE_PRODUCT_MESSAGE;
    }

    @Override
    public List<Product> getProductsById(String[] productsId) throws DaoException {
        String generatedSQL = generateSQLByProductsId(productsId);
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(generatedSQL)) {
            initializePreparedStatement(preparedStatement, productsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractProductsFromResultSet(resultSet);
        } catch (SQLException e) {
            log.error(CANNOT_GET_PRODUCTS_BY_ID_MESSAGE + Arrays.toString(productsId), e);
            throw new DaoException(CANNOT_GET_PRODUCTS_BY_ID_MESSAGE + Arrays.toString(productsId), e);
        }
    }

    @Override
    public Optional<Product> getProductById(String id) throws DaoException {
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_PRODUCTS_BY_ID + PRODUCT_ID_GENERATED_FOR_SQL_PART)) {
            preparedStatement.setLong(1, Long.parseLong(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildProductByResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error(getCannotFoundProductByIdMessage() + id, e);
            throw new DaoException(getCannotFoundProductByIdMessage() + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() throws DaoException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_PRODUCTS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(buildProductByResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error(getCannotFindAllProductsMessage(), e);
            throw new DaoException(getCannotFindAllProductsMessage(), e);
        }
        return products;
    }

    @Override
    public Product updateById(String id, String name, double price, boolean isPromotion) throws DaoException {
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PRODUCT_BY_ID)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setBoolean(3, isPromotion);
            preparedStatement.setLong(4, Long.parseLong(id));
            int countRowsUpdated = preparedStatement.executeUpdate();
            if (countRowsUpdated > 0) {
                return new Product.Builder().withId(Long.parseLong(id)).withName(name).withPrice(price).withIsPromotion(isPromotion).build();
            }
        } catch (SQLException e) {
            log.error(getCannotUpdateProductByIdMessage(), e);
            throw new DaoException(getCannotUpdateProductByIdMessage(), e);
        }
        throw new DaoException(getCannotUpdateProductByIdMessage());
    }

    @Override
    public boolean deleteById(String id) throws DaoException {
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_PRODUCT_BY_ID)) {
            preparedStatement.setLong(1, Long.parseLong(id));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(getCannotDeteleteProductByIdMessage() + id, e);
            throw new DaoException(getCannotDeteleteProductByIdMessage() + id, e);
        }
    }

    @Override
    public Product create(String name, double price, boolean isPromotion) throws DaoException {
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setBoolean(3, isPromotion);
            int countRowsCreated = preparedStatement.executeUpdate();
            if (countRowsCreated > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return buildProductByResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error(getCannotCreateProductMessage(), e);
            throw new DaoException(getCannotCreateProductMessage(), e);
        }
        throw new DaoException(getCannotCreateProductMessage());
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
                generatedSQL.append(PRODUCT_ID_GENERATED_FOR_SQL_PART);
            } else {
                generatedSQL.append(PRODUCT_ID_GENERATED_FOR_SQL_PART).append(OR_OPERATOR_FOR_SQL);
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
        return new Product.Builder().withId(resultSet.getLong(1)).withName(resultSet.getString(2)).withPrice(resultSet.getDouble(3)).withIsPromotion(resultSet.getBoolean(4)).build();
    }
}
