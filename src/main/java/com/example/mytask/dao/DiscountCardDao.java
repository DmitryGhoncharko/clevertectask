package com.example.mytask.dao;

import com.example.mytask.exception.DaoException;
import com.example.mytask.model.DiscountCard;

import java.util.Optional;

public interface DiscountCardDao {
    Optional<DiscountCard> getCardById(String id) throws DaoException;
}
