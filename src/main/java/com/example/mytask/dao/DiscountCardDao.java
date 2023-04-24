package com.example.mytask.dao;

import com.example.mytask.exception.DaoException;
import com.example.mytask.model.DiscountCard;

import java.util.List;
import java.util.Optional;

public interface DiscountCardDao {
    Optional<DiscountCard> getCardById(String id) throws DaoException;

    List<DiscountCard> findAll() throws DaoException;

    DiscountCard updateById(String id, double discountValue) throws DaoException;

    boolean deleteById(String id) throws DaoException;

    DiscountCard create(double discountValue) throws DaoException;
}
