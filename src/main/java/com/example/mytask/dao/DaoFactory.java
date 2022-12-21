package com.example.mytask.dao;

public interface DaoFactory {
    ProductDao createProductDao();

    DiscountCardDao createDiscountCardDao();
}
