package com.example.mytask.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleDiscountCardServiceValidator implements DiscountCardServiceValidator {
    public static final String REGEX_FIND_ONLY_NUMBERS = "^[0-9]{1,}$";

    @Override
    public boolean validate(String id) {
        if (id == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_FIND_ONLY_NUMBERS);
        Matcher matcher = pattern.matcher(id);
        return matcher.find();
    }

    @Override
    public boolean validate(double discountValue) {
        return discountValue >= 0;
    }

    @Override
    public boolean validate(String id, double discountValue) {
        return validate(id) && validate(discountValue);
    }
}
