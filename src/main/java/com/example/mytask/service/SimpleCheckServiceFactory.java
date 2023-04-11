package com.example.mytask.service;

import com.example.mytask.connection.ConnectionPoolFactory;
import com.example.mytask.connection.HikariCPConnectionPoolFactory;
import com.example.mytask.dao.DaoFactory;
import com.example.mytask.dao.PostgresqlDaoFactory;
import com.example.mytask.dao.proxy.PostgresqlDaoFactoryProxy;
import com.example.mytask.validator.CheckServiceValidatorFactory;
import com.example.mytask.validator.SimpleCheckServiceValidatorFactory;


public class SimpleCheckServiceFactory implements CheckServiceFactory {

    private final ConnectionPoolFactory connectionPoolFactory = new HikariCPConnectionPoolFactory();
    private final DaoFactory daoFactory = new PostgresqlDaoFactory(connectionPoolFactory);
    private final DaoFactory daoFactoryProxy = new PostgresqlDaoFactoryProxy(connectionPoolFactory);
    private final CheckServiceValidatorFactory checkServiceValidatorFactory = new SimpleCheckServiceValidatorFactory();

    @Override
    public CheckService createCheckService() {
        return new SimpleCheckService(daoFactory.createProductDao(), daoFactory.createDiscountCardDao(), checkServiceValidatorFactory.createValidator());
    }
}
