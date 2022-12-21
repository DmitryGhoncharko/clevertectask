package com.example.mytask.dao;

import com.example.mytask.connection.ConnectionPool;
import com.example.mytask.connection.ConnectionPoolFactory;

public class PostgresqlDaoFactory implements DaoFactory {

    private final ConnectionPool connectionPool;

    public PostgresqlDaoFactory(ConnectionPoolFactory connectionPoolFactory) {
        this.connectionPool = connectionPoolFactory.createPool();
    }

    @Override
    public ProductDao createProductDao() {
        return new PostgresqlProductDao(connectionPool);
    }

    @Override
    public DiscountCardDao createDiscountCardDao() {
        return new PostgresqlDiscountCardDao(connectionPool);
    }
}
