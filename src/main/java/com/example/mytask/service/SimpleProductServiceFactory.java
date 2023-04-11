package com.example.mytask.service;

import com.example.mytask.connection.ConnectionPoolFactory;
import com.example.mytask.connection.HikariCPConnectionPoolFactory;
import com.example.mytask.dao.DaoFactory;
import com.example.mytask.dao.proxy.PostgresqlDaoFactoryProxy;
import com.example.mytask.validator.ProductServiceValidatorFactory;
import com.example.mytask.validator.SimpleProductServiceValidatorFactory;

public class SimpleProductServiceFactory implements ProductServiceFactory {
    private final ConnectionPoolFactory connectionPoolFactory = new HikariCPConnectionPoolFactory();
    private final DaoFactory daoFactory = new PostgresqlDaoFactoryProxy(connectionPoolFactory);
    private final ProductServiceValidatorFactory productServiceValidatorFactory = new SimpleProductServiceValidatorFactory();

    @Override
    public ProductService createService() {
        return new SimpleProductService(daoFactory.createProductDao(), productServiceValidatorFactory.create());
    }
}
