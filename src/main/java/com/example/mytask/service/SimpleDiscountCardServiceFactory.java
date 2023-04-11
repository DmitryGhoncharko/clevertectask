package com.example.mytask.service;

import com.example.mytask.connection.ConnectionPoolFactory;
import com.example.mytask.connection.HikariCPConnectionPoolFactory;
import com.example.mytask.dao.DaoFactory;
import com.example.mytask.dao.proxy.PostgresqlDaoFactoryProxy;
import com.example.mytask.validator.DiscountCardServiceValidatorFactory;
import com.example.mytask.validator.SimpleDiscountCardServiceValidatorFactory;

public class SimpleDiscountCardServiceFactory implements DiscountCardServiceFactory {
    private final ConnectionPoolFactory connectionPoolFactory = new HikariCPConnectionPoolFactory();
    private final DaoFactory daoFactory = new PostgresqlDaoFactoryProxy(connectionPoolFactory);
    private final DiscountCardServiceValidatorFactory discountCardServiceValidatorFactory = new SimpleDiscountCardServiceValidatorFactory();

    @Override
    public DiscountCardService createDiscountCardService() {
        return new SimpleDiscountCardService(daoFactory.createDiscountCardDao(), discountCardServiceValidatorFactory.createValidator());
    }
}
