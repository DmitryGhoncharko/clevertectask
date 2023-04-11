package com.example.mytask.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleProductServiceValidator implements ProductServiceValidator{
    public static final String REGEX_FIND_ONLY_NUMBERS = "^[0-9]{1,}$";
    @Override
    public boolean validate(String id) {
        if(id == null){
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_FIND_ONLY_NUMBERS);
        Matcher matcher = pattern.matcher(id);
        return matcher.find();
    }

    @Override
    public boolean validate(String id, String name, double price, boolean isPromotion) {
        return validate(id) && validate(name,price,isPromotion);
    }

    @Override
    public boolean validate(String name, double price, boolean isPromotion) {
        return name!=null && price>=0;
    }
}
