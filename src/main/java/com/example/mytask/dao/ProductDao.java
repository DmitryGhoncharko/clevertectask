package com.example.mytask.dao;

import com.example.mytask.exception.DaoException;
import com.example.mytask.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    List<Product> getProductsById(String[] productsId) throws DaoException;

    Optional<Product> getProductById(String id)throws DaoException;

    List<Product> findAll() throws DaoException;

    Product updateById(String id, String name, double price, boolean isPromotion) throws DaoException;

    boolean deleteById(String id) throws DaoException;

    Product create(String name, double price, boolean isPromotion) throws DaoException;
}
