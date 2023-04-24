package com.example.mytask.validator;

public class SimpleProductServiceValidatorFactory implements ProductServiceValidatorFactory{
    @Override
    public ProductServiceValidator create() {
        return new SimpleProductServiceValidator();
    }
}
