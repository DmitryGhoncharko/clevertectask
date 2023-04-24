package com.example.mytask.service;

import com.example.mytask.exception.DaoException;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.model.DiscountCard;

import java.util.List;
import java.util.Optional;

public interface DiscountCardService {
    Optional<DiscountCard> getCardById(String id) throws ServiceException;

    List<DiscountCard> findAll() throws ServiceException;

    DiscountCard updateById(String id, double discountValue) throws ServiceException;

    boolean deleteById(String id) throws ServiceException;

    DiscountCard create(double discountValue) throws ServiceException;
}
