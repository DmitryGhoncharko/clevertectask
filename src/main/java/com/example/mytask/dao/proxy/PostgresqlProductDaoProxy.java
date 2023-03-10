package com.example.mytask.dao.proxy;

import com.example.mytask.cache.Cache;
import com.example.mytask.dao.ProductDao;
import com.example.mytask.exception.DaoException;
import com.example.mytask.model.Product;
import lombok.RequiredArgsConstructor;


import java.util.Collections;
import java.util.List;

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
}
