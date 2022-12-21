package com.example.mytask.dao;

import com.example.mytask.exception.DaoException;
import com.example.mytask.model.DiscountCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SimpleDiscountCardDao implements DiscountCardDao {
    private static final String SQL_GET_CARD_BY_ID = "select discount_card_id, discount_card_discount_value from discount_card where discount_card_id = ?";
    private final Connection connection;
    @Override
    public Optional<DiscountCard> getCardById(String id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_CARD_BY_ID)) {
            preparedStatement.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractDiscountCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error("Cannot find discount card by card id = " + id, e);
            throw new DaoException("Cannot find discount card by card id = " + id, e);
        }
        return Optional.empty();
    }

    private DiscountCard extractDiscountCardFromResultSet(ResultSet resultSet) throws SQLException {
        return new DiscountCard.Builder().
                withId(resultSet.getLong(1)).
                withDiscount(resultSet.getDouble(2)).
                build();
    }
}
