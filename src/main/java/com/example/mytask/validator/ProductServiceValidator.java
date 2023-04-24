package com.example.mytask.validator;

public interface ProductServiceValidator {
    boolean validate(String id);

    boolean validate(String id, String name, double price, boolean isPromotion);

    boolean validate(String name, double price, boolean isPromotion);
}
