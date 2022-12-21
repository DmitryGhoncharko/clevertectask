package com.example.mytask.validator;

public class SimpleCheckServiceValidatorFactory implements CheckServiceValidatorFactory{
    @Override
    public CheckServiceValidator createValidator() {
        return new SimpleCheckServiceValidator();
    }
}
