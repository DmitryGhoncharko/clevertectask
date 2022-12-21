package com.example.mytask.validator;

public class SimpleCheckServiceValidator implements CheckServiceValidator{
    @Override
    public boolean validate(String[] productsId, String[] countProductsOnEachId, String discountCardId) {
        return productsId!=null && countProductsOnEachId!=null;
    }
}
