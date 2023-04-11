package com.example.mytask.service;

import com.example.mytask.dao.DiscountCardDao;
import com.example.mytask.exception.DaoException;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.model.DiscountCard;
import com.example.mytask.validator.DiscountCardServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SimpleDiscountCardService implements DiscountCardService {
    public static final String CANNOT_GET_CARD_BY_ID_MESSAGE = "Cannot get card by id";
    public static final String CANNOT_FIND_ALL_DISCOUNT_CARDS_MESSAGE = "Cannot find all discount cards";
    public static final String CANNOT_UPDATE_DISCOUNT_CARD_BY_ID_MESSAGE = "Cannot update discount card by id";
    public static final String CANNOT_DELETE_DISCOUNT_CARD_BY_ID_MESSAGE = "Cannot delete discount card by id";
    public static final String CANNOT_CREATE_DISCOUNT_CARD_MESSAGE = "Cannot create discount card";
    private final DiscountCardDao discountCardDao;
    private final DiscountCardServiceValidator discountCardServiceValidator;

    @Override
    public Optional<DiscountCard> getCardById(String id) throws ServiceException {
        if (!discountCardServiceValidator.validate(id)) {
            return Optional.empty();
        }
        try {
            return discountCardDao.getCardById(id);
        } catch (DaoException e) {
            log.error(CANNOT_GET_CARD_BY_ID_MESSAGE, e);
            throw new ServiceException(CANNOT_GET_CARD_BY_ID_MESSAGE, e);
        }
    }

    @Override
    public List<DiscountCard> findAll() throws ServiceException {
        try {
            return discountCardDao.findAll();
        } catch (DaoException e) {
            log.error(CANNOT_FIND_ALL_DISCOUNT_CARDS_MESSAGE, e);
            throw new ServiceException(CANNOT_FIND_ALL_DISCOUNT_CARDS_MESSAGE, e);
        }
    }

    @Override
    public DiscountCard updateById(String id, double discountValue) throws ServiceException {
        if (!discountCardServiceValidator.validate(id, discountValue)) {
            throw new ServiceException(CANNOT_UPDATE_DISCOUNT_CARD_BY_ID_MESSAGE);
        }
        try {
            return discountCardDao.updateById(id, discountValue);
        } catch (DaoException e) {
            log.error(CANNOT_UPDATE_DISCOUNT_CARD_BY_ID_MESSAGE, e);
            throw new ServiceException(CANNOT_GET_CARD_BY_ID_MESSAGE, e);
        }

    }

    @Override
    public boolean deleteById(String id) throws ServiceException {
        if (!discountCardServiceValidator.validate(id)) {
            return false;
        }
        try {
            return discountCardDao.deleteById(id);
        } catch (DaoException e) {
            log.error(CANNOT_DELETE_DISCOUNT_CARD_BY_ID_MESSAGE, e);
            throw new ServiceException(CANNOT_GET_CARD_BY_ID_MESSAGE, e);
        }
    }

    @Override
    public DiscountCard create(double discountValue) throws ServiceException {
        if (!discountCardServiceValidator.validate(discountValue)) {
            throw new ServiceException(CANNOT_CREATE_DISCOUNT_CARD_MESSAGE);
        }
        try {
            return discountCardDao.create(discountValue);
        } catch (DaoException e) {
            log.error(CANNOT_CREATE_DISCOUNT_CARD_MESSAGE, e);
            throw new ServiceException(CANNOT_CREATE_DISCOUNT_CARD_MESSAGE, e);
        }
    }
}
