package com.example.mytask.dao.proxy;

import com.example.mytask.cache.Cache;
import com.example.mytask.dao.ProductDao;
import com.example.mytask.exception.DaoException;
import com.example.mytask.model.Product;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostgresqlProductDaoProxy implements ProductDao {
    private final ProductDao productDao;
    private final Cache<String[], List<Product>> cache;

    @Override
    public List<Product> getProductsById(String[] productsId) throws DaoException {
        for (String id : productsId) {
            if (id != null) {
                List<Product> product = cache.get(productsId);
                if (product != null) {
                    return product;
                } else {
                    List<Product> productList = productDao.getProductsById(productsId);
                    cache.put(productsId, productList);
                    return productList;
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Product> getProductById(String id) throws DaoException {
        return productDao.getProductById(id);
    }

    @Override
    public List<Product> findAll() throws DaoException {
        return productDao.findAll();
    }

    @Override
    public Product updateById(String id, String name, double price, boolean isPromotion) throws DaoException {
        return productDao.updateById(id, name, price, isPromotion);
    }

    @Override
    public boolean deleteById(String id) throws DaoException {
        return productDao.deleteById(id);
    }

    @Override
    public Product create(String name, double price, boolean isPromotion) throws DaoException {
        return productDao.create(name, price, isPromotion);
    }
}
