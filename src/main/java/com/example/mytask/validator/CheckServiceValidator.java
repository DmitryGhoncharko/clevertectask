package com.example.mytask.validator;

public interface CheckServiceValidator {
    boolean validate(String[] productsId, String[] countProductsOnEachId, String discountCardId);
}
