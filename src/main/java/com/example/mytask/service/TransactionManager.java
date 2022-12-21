package com.example.mytask.service;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private TransactionManager(){

    }
    public static void startTransaction(Connection connection){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void commitTransaction(Connection connection){
        try {
            if(!connection.getAutoCommit()){
                connection.commit();
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void rollbackTransaction(Connection connection){
        try {
            connection.rollback();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
