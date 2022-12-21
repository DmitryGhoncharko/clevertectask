package com.example.mytask.service;

import com.example.mytask.dto.CheckDTO;
import com.example.mytask.exception.ServiceException;

public interface CheckService {
    CheckDTO getCheckByProductsIdsAndDiscountCardIdI(String[] productsId,String[] countProductsOnEachId, String discountCardId) throws ServiceException;
}
