package com.example.mytask.service;

import com.example.mytask.exception.DaoException;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.model.DiscountCard;
import com.example.mytask.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getProductById(String id)throws ServiceException;

    List<Product> findAll() throws ServiceException;

    Product updateById(String id, String name, double price, boolean isPromotion) throws ServiceException;

    boolean deleteById(String id) throws ServiceException;

    Product create(String name, double price, boolean isPromotion) throws ServiceException;
}
