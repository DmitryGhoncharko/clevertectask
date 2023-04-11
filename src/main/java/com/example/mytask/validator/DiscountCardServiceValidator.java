package com.example.mytask.validator;

public interface DiscountCardServiceValidator {
    boolean validate(String id);

    boolean validate(double discountValue);

    boolean validate(String id, double discountValue);
}
