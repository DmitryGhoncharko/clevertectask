package com.example.mytask.dao.proxy;

import com.example.mytask.cache.Cache;
import com.example.mytask.cache.CacheFactory;
import com.example.mytask.connection.ConnectionPool;
import com.example.mytask.connection.ConnectionPoolFactory;
import com.example.mytask.dao.*;
import com.example.mytask.model.DiscountCard;
import com.example.mytask.model.Product;

import java.util.List;

public class PostgresqlDaoFactoryProxy implements DaoFactory {
    private final ConnectionPool connectionPool;
    private static final Cache<String[], List<Product>> PRODUCT_CACHE = new CacheFactory<String[], List<Product>>().getCacheByConfiguration();
    private static final Cache<Long, DiscountCard> DISCOUNT_CARD_CACHE = new CacheFactory<Long, DiscountCard>().getCacheByConfiguration();

    public PostgresqlDaoFactoryProxy(ConnectionPoolFactory connectionPoolFactory) {
        this.connectionPool = connectionPoolFactory.createPool();
    }

    @Override
    public ProductDao createProductDao() {
        return new PostgresqlProductDaoProxy(new PostgresqlProductDao(connectionPool), PRODUCT_CACHE);
    }

    @Override
    public DiscountCardDao createDiscountCardDao() {
        return new PostgresqlDiscountCardDaoProxy(new PostgresqlDiscountCardDao(connectionPool), DISCOUNT_CARD_CACHE);
    }
}



