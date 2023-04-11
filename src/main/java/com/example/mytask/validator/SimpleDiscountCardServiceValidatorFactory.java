package com.example.mytask.validator;

public class SimpleDiscountCardServiceValidatorFactory implements DiscountCardServiceValidatorFactory{
    @Override
    public DiscountCardServiceValidator createValidator() {
        return new SimpleDiscountCardServiceValidator();
    }
}
