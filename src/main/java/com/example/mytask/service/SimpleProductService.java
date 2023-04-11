package com.example.mytask.service;

import com.example.mytask.dao.ProductDao;
import com.example.mytask.exception.DaoException;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.model.Product;
import com.example.mytask.validator.ProductServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SimpleProductService implements ProductService {
    private static final String CANNOT_GET_PRODUCT_BY_ID_MESSAGE = "Cannot get product by id";
    private static final String CANNOT_FIND_ALL_PRODUCTS_MESSAGE = "Cannot find all products";
    private static final String CANNOT_UPDATE_PRODUCT_BY_ID_MESSAGE = "Cannot update product by id";
    private static final String CANNOT_DELETE_PRODUCT_BY_ID_MESSAGE = "Cannot dalete product by id";
    private static final String CANNOT_CREATE_PRODUCT_MESSAGE = "Cannot create product";
    private final ProductDao productDao;
    private final ProductServiceValidator productServiceValidator;

    public static String getCannotUpdateProductByIdMessage() {
        return CANNOT_UPDATE_PRODUCT_BY_ID_MESSAGE;
    }

    public static String getCannotDeleteProductByIdMessage() {
        return CANNOT_DELETE_PRODUCT_BY_ID_MESSAGE;
    }

    public static String getCannotCreateProductMessage() {
        return CANNOT_CREATE_PRODUCT_MESSAGE;
    }

    @Override
    public Optional<Product> getProductById(String id) throws ServiceException {
        if (!productServiceValidator.validate(id)) {
            return Optional.empty();
        }
        try {
            return productDao.getProductById(id);
        } catch (DaoException e) {
            log.error(CANNOT_GET_PRODUCT_BY_ID_MESSAGE, e);
            throw new ServiceException(CANNOT_GET_PRODUCT_BY_ID_MESSAGE, e);
        }
    }

    @Override
    public List<Product> findAll() throws ServiceException {
        try {
            return productDao.findAll();
        } catch (DaoException e) {
            log.error(CANNOT_FIND_ALL_PRODUCTS_MESSAGE, e);
            throw new ServiceException(CANNOT_FIND_ALL_PRODUCTS_MESSAGE, e);
        }
    }

    @Override
    public Product updateById(String id, String name, double price, boolean isPromotion) throws ServiceException {
        if (!productServiceValidator.validate(id, name, price, isPromotion)) {
            throw new ServiceException();
        }
        try {
            return productDao.updateById(id, name, price, isPromotion);
        } catch (DaoException e) {
            log.error(getCannotUpdateProductByIdMessage(), e);
            throw new ServiceException(getCannotUpdateProductByIdMessage(), e);
        }
    }

    @Override
    public boolean deleteById(String id) throws ServiceException {
        if (!productServiceValidator.validate(id)) {
            return false;
        }
        try {
            return productDao.deleteById(id);
        } catch (DaoException e) {
            log.error(getCannotDeleteProductByIdMessage(), e);
            throw new ServiceException(getCannotDeleteProductByIdMessage(), e);
        }
    }

    @Override
    public Product create(String name, double price, boolean isPromotion) throws ServiceException {
        if (!productServiceValidator.validate(name, price, isPromotion)) {
            throw new ServiceException();
        }
        try {
            return productDao.create(name, price, isPromotion);
        } catch (DaoException e) {
            log.error(getCannotCreateProductMessage(), e);
            throw new ServiceException(getCannotCreateProductMessage(), e);
        }
    }
}
