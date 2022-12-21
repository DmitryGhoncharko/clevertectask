package com.example.mytask.service;

import com.example.mytask.dao.*;
import com.example.mytask.dto.CheckDTO;
import com.example.mytask.dto.ProductDTO;
import com.example.mytask.exception.DaoException;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.exception.ValidationFailedException;
import com.example.mytask.model.DiscountCard;
import com.example.mytask.model.Product;
import com.example.mytask.validator.CheckServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SimpleCheckService implements CheckService {
    private static final String TEN_PERCENT_DISCOUNT_VALUE_DEFAULT = "0.1";
    private static final String DEFAULT_ZERO_VALUE_FOR_FINAL_PRICE = "0.0";
    private static final int COUNT_PRODUCTS_FOR_DISCOUNT_TEN_PERCENT_ON_THIS_PRODUCT = 5;
    private static final int NUMBERS_AFTER_COME_PARAM_ROUNDING_VALUE = 3;
    private final ProductDao productDao;
    private final DiscountCardDao discountCardDao;

    private final CheckServiceValidator checkServiceValidator;

    @Override
    public CheckDTO getCheckByProductsIdsAndDiscountCardIdI(String[] productsId, String[] countProductsOnEachId, String discountCardId) throws ServiceException, ValidationFailedException {
        if (!checkServiceValidator.validate(productsId, countProductsOnEachId, discountCardId)) {
            log.error("Validation failed" + " productsId=" + productsId + " countProductsEachId= " + countProductsOnEachId + " discountCardId= " + discountCardId);
            throw new ValidationFailedException("Validation failed" + " productsId=" + productsId + " countProductsEachId= " + countProductsOnEachId + " discountCardId= " + discountCardId);
        }
        try {
            List<Product> products = productDao.getProductsById(productsId);
            List<ProductDTO> productDTOList = initializeProductsDto(products, countProductsOnEachId);
            double totalPrice = calculateTotalPrice(productDTOList);
            if (discountCardId != null) {
                Optional<DiscountCard> discountCard = discountCardDao.getCardById(discountCardId);
                if (discountCard.isPresent()) {
                    return extractCheckDTOIfDiscountCardIsPresent(productDTOList, totalPrice, discountCard);
                } else {
                    return extractCheckDTO(productDTOList, totalPrice);
                }
            } else {
                return extractCheckDTO(productDTOList, totalPrice);
            }
        } catch (DaoException e) {
            log.error("Cannot create check productsId = " + Arrays.toString(productsId) + " discountCardId = " + discountCardId, e);
            throw new ServiceException("Cannot create check productsId = " + Arrays.toString(productsId) + " discountCardId = " + discountCardId, e);
        }
    }

    private CheckDTO extractCheckDTOIfDiscountCardIsPresent(List<ProductDTO> productDTOList, double totalPrice, Optional<DiscountCard> discountCard) {
        double totalPriceAfterDiscount = new BigDecimal(totalPrice).subtract(new BigDecimal(totalPrice).multiply(BigDecimal.valueOf(discountCard.get().getDiscount()).divide(BigDecimal.valueOf(100)))).setScale(NUMBERS_AFTER_COME_PARAM_ROUNDING_VALUE, RoundingMode.CEILING).doubleValue();
        DiscountCard curDiscountCard = discountCard.get();
        return new CheckDTO.Builder().
                withProductsDTO(productDTOList).
                withDiscountCard(curDiscountCard).
                withTotalPrice(totalPriceAfterDiscount).
                build();
    }

    private CheckDTO extractCheckDTO(List<ProductDTO> productDTOList, double totalPrice) {
        return new CheckDTO.Builder().
                withProductsDTO(productDTOList).
                withTotalPrice(totalPrice).
                build();
    }

    private double calculateTotalPrice(List<ProductDTO> productDTOList) {
        BigDecimal finalPrice = new BigDecimal(DEFAULT_ZERO_VALUE_FOR_FINAL_PRICE);
        for (ProductDTO productDTO : productDTOList) {
            finalPrice = finalPrice.add(BigDecimal.valueOf(productDTO.getFinalPrise())).setScale(3, RoundingMode.CEILING);
        }
        return finalPrice.doubleValue();
    }

    private List<ProductDTO> initializeProductsDto(List<Product> products, String[] countProductsOnEachId) {
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int countCurrentProduct = Integer.parseInt(countProductsOnEachId[i]);
            if (product.getIsPromotion() && countCurrentProduct > COUNT_PRODUCTS_FOR_DISCOUNT_TEN_PERCENT_ON_THIS_PRODUCT) {
                addProductDTOToListIfProductIsPromotion(productDTOList, product, countCurrentProduct);
            } else {
                addProductDTOToListIfProductIsNotPromotion(productDTOList, product, countCurrentProduct);
            }
        }
        return productDTOList;
    }

    private void addProductDTOToListIfProductIsNotPromotion(List<ProductDTO> productDTOList, Product product, int countCurrentProduct) {
        BigDecimal productPriceAsBigDecimal = BigDecimal.valueOf(product.getPrice());
        double finalPrice = productPriceAsBigDecimal.multiply(new BigDecimal(countCurrentProduct)).
                setScale(NUMBERS_AFTER_COME_PARAM_ROUNDING_VALUE, RoundingMode.CEILING).
                doubleValue();
        ProductDTO productDTO = extractProductDTO(product, countCurrentProduct, finalPrice);
        productDTOList.add(productDTO);
    }

    private void addProductDTOToListIfProductIsPromotion(List<ProductDTO> productDTOList, Product product, int countCurrentProduct) {
        BigDecimal productPriceAsBigDecimal = BigDecimal.valueOf(product.getPrice());
        double finalPrice = productPriceAsBigDecimal.multiply(new BigDecimal(countCurrentProduct)).
                subtract(new BigDecimal(countCurrentProduct).
                        multiply(productPriceAsBigDecimal).
                        multiply(new BigDecimal(TEN_PERCENT_DISCOUNT_VALUE_DEFAULT))).
                setScale(NUMBERS_AFTER_COME_PARAM_ROUNDING_VALUE, RoundingMode.CEILING).
                doubleValue();
        ProductDTO productDTO = extractProductDTO(product, countCurrentProduct, finalPrice);
        productDTOList.add(productDTO);
    }

    private ProductDTO extractProductDTO(Product product, int countCurrentProduct, double finalPrice) {
        return new ProductDTO.Builder().
                withProduct(product).
                withCount(countCurrentProduct).
                withFinalPrice(finalPrice).
                build();
    }
}
