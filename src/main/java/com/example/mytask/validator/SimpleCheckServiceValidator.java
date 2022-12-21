package com.example.mytask.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleCheckServiceValidator implements CheckServiceValidator {

    public static final String REGEX_FIND_ONLY_NUMBERS = "^[0-9]{1,}$";

    @Override
    public boolean validate(String[] productsId, String[] countProductsOnEachId, String discountCardId) {
        if (productsId == null || countProductsOnEachId == null) {
            return false;
        }
        if (!validateArraysStringData(productsId) || !validateArraysStringData(countProductsOnEachId)) return false;
        return true;
    }

    private static boolean validateArraysStringData(String[] productsId) {
        Pattern pattern = Pattern.compile(REGEX_FIND_ONLY_NUMBERS);
        for (String data : productsId) {
            if (data == null) {
                return false;
            } else {
                Matcher matcher = pattern.matcher(data);
                if (!matcher.find()) {
                    return false;
                }
            }
        }
        return true;
    }
}
