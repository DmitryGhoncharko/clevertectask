package com.example.mytask.dao;

import com.example.mytask.connection.ConnectionPool;
import com.example.mytask.exception.DaoException;
import com.example.mytask.model.DiscountCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PostgresqlDiscountCardDao implements DiscountCardDao {
    private static final String CANNOT_UPDATE_DISCOUNT_CARD_BY_ID_CARD_ID_MESSAGE = "Cannot update discount card by id card id = ";
    private static final String DISCOUNT_VALUE_ARG = " discount value = ";
    private static final String CANNOT_DELETE_DISCOUNT_CARD_BY_ID_MESSAGE = "Cannot delete discount card by id, id = ";
    private static final String CANNOT_FIND_ALL_DISCOUNT_CARDS_MESAGE = "Cannot find all discount cards";
    private static final String CANNOT_CREATE_DISCOUNT_CARD_DISCOUNT_CARD_VALUE_MESSAGE = "Cannot create discount card, discount card value = ";
    private static final String CANNOT_FIND_DISCOUNT_CARD_BY_CARD_ID_MESSAGE = "Cannot find discount card by card id = ";
    private static final String SQL_GET_CARD_BY_ID = "select discount_card_id, discount_card_discount_value from discount_card where discount_card_id = ?";
    private static final String SQL_FIND_ALL_DISCOUNT_CARD = "select discount_card_id, discount_card_discount_value from discount_card";
    private static final String SQL_UPDATE_DISCOUNT_CARD_BY_ID = "update discount_card set discount_card_discount_value = ? where discount_card_id = ?";
    private static final String SQL_DELETE_DISCOUNT_CARD_BY_ID = "delete from discount_card where discount_card_id = ?";
    private static final String SQL_CREATE_DISCOUNT_CARD = "insert into discount_card(discount_card_discount_value) values(?)";
    private final ConnectionPool connectionPool;

    public static String getCannotUpdateDiscountCardByIdCardIdMessage() {
        return CANNOT_UPDATE_DISCOUNT_CARD_BY_ID_CARD_ID_MESSAGE;
    }

    public static String getDiscountValueArg() {
        return DISCOUNT_VALUE_ARG;
    }

    public static String getCannotDeleteDiscountCardByIdMessage() {
        return CANNOT_DELETE_DISCOUNT_CARD_BY_ID_MESSAGE;
    }

    public static String getCannotFindAllDiscountCardsMesage() {
        return CANNOT_FIND_ALL_DISCOUNT_CARDS_MESAGE;
    }

    public static String getCannotCreateDiscountCardDiscountCardValueMessage() {
        return CANNOT_CREATE_DISCOUNT_CARD_DISCOUNT_CARD_VALUE_MESSAGE;
    }

    @Override
    public Optional<DiscountCard> getCardById(String id) throws DaoException {
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_CARD_BY_ID)) {
            preparedStatement.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractDiscountCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error(CANNOT_FIND_DISCOUNT_CARD_BY_CARD_ID_MESSAGE + id, e);
            throw new DaoException(CANNOT_FIND_DISCOUNT_CARD_BY_CARD_ID_MESSAGE + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<DiscountCard> findAll() throws DaoException {
        List<DiscountCard> discountCards = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_DISCOUNT_CARD)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                discountCards.add(extractDiscountCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error(getCannotFindAllDiscountCardsMesage(), e);
            throw new DaoException(getCannotFindAllDiscountCardsMesage(), e);
        }
        return discountCards;
    }

    @Override
    public DiscountCard updateById(String id, double discountValue) throws DaoException {
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_DISCOUNT_CARD_BY_ID)) {
            preparedStatement.setDouble(1, discountValue);
            preparedStatement.setLong(2, Long.parseLong(id));
            int countRowsUpdated = preparedStatement.executeUpdate();
            if (countRowsUpdated > 0) {
                return new DiscountCard.Builder().withId(Long.parseLong(id)).withDiscount(discountValue).build();
            }
        } catch (SQLException e) {
            log.error(getCannotUpdateDiscountCardByIdCardIdMessage() + id + getDiscountValueArg() + discountValue, e);
            throw new DaoException(getCannotUpdateDiscountCardByIdCardIdMessage() + id + getDiscountValueArg() + discountValue, e);
        }
        throw new DaoException(getCannotUpdateDiscountCardByIdCardIdMessage() + id + getDiscountValueArg() + discountValue);
    }

    @Override
    public boolean deleteById(String id) throws DaoException {
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_DISCOUNT_CARD_BY_ID)) {
            preparedStatement.setLong(1, Long.parseLong(id));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(getCannotDeleteDiscountCardByIdMessage() + id, e);
            throw new DaoException(getCannotDeleteDiscountCardByIdMessage() + id, e);
        }
    }

    @Override
    public DiscountCard create(double discountValue) throws DaoException {
        try (Connection connection = connectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_DISCOUNT_CARD, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, discountValue);
            int countRowsCreated = preparedStatement.executeUpdate();
            if (countRowsCreated > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return extractDiscountCardFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error(getCannotCreateDiscountCardDiscountCardValueMessage() + discountValue, e);
            throw new DaoException(getCannotDeleteDiscountCardByIdMessage() + discountValue, e);
        }
        throw new DaoException(getCannotDeleteDiscountCardByIdMessage() + discountValue);
    }

    private DiscountCard extractDiscountCardFromResultSet(ResultSet resultSet) throws SQLException {
        return new DiscountCard.Builder().withId(resultSet.getLong(1)).withDiscount(resultSet.getDouble(2)).build();
    }
}
