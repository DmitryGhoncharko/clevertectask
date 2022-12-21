package com.example.mytask.service;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.exception.ValidationFailedException;

public interface CheckService {
    CheckDTO getCheckByProductsIdsAndDiscountCardId(String[] productsId, String[] countProductsOnEachId, String discountCardId) throws ServiceException, ValidationFailedException;
}
