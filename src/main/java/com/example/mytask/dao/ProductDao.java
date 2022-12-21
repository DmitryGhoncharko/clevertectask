package com.example.mytask.dao;

import com.example.mytask.exception.DaoException;
import com.example.mytask.model.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getProductsById(String[] productsId) throws DaoException;
}
