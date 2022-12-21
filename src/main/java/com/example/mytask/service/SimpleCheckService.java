package com.example.mytask.service;

import com.example.mytask.connection.ConnectionPool;
import com.example.mytask.dao.DiscountCardDao;
import com.example.mytask.dao.ProductDao;
import com.example.mytask.dao.SimpleDiscountCardDao;
import com.example.mytask.dao.SimpleProductDao;
import com.example.mytask.dto.CheckDTO;
import com.example.mytask.dto.ProductDTO;
import com.example.mytask.exception.DaoException;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.model.DiscountCard;
import com.example.mytask.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SimpleCheckService implements CheckService {


    private final ConnectionPool connectionPool;

    @Override
    public CheckDTO getCheckByProductsIdsAndDiscountCardIdI(String[] productsId, String[] countProductsOnEachId, String discountCardId) throws ServiceException {
        try (Connection connection = connectionPool.getConnection()) {
            TransactionManager.startTransaction(connection);
            ProductDao productDao = new SimpleProductDao(connection);
            DiscountCardDao discountCardDao = new SimpleDiscountCardDao(connection);
            List<Product> products = productDao.getProductsById(productsId);
            Optional<DiscountCard> discountCard = discountCardDao.getCardById(discountCardId);
            List<ProductDTO> productDTOList = initializeProductsDto(products, countProductsOnEachId);
            double totalPrice = calculateTotalPrice(productDTOList);
            if (discountCard.isPresent()) {
                double totalPriceAfterDiscount = new BigDecimal(totalPrice).subtract(new BigDecimal(totalPrice).multiply(BigDecimal.valueOf(discountCard.get().getDiscount()).divide(BigDecimal.valueOf(100)))).setScale(3, RoundingMode.CEILING).doubleValue();
                DiscountCard curDiscountCard = discountCard.get();
                TransactionManager.commitTransaction(connection);
                return new CheckDTO.Builder().
                        withProductsDTO(productDTOList).
                        withDiscountCard(curDiscountCard).
                        withTotalPrice(totalPriceAfterDiscount).
                        build();
            } else {
                TransactionManager.commitTransaction(connection);
                return new CheckDTO.Builder().
                        withProductsDTO(productDTOList).
                        withTotalPrice(totalPrice).
                        build();
            }
        } catch (DaoException e) {
            log.error("Cannot create check productsId = " + Arrays.toString(productsId) + " discountCardId = " + discountCardId, e);
            throw new ServiceException("Cannot create check productsId = " + Arrays.toString(productsId) + " discountCardId = " + discountCardId, e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private double calculateTotalPrice(List<ProductDTO> productDTOList) {
        BigDecimal finalPrice = new BigDecimal("0.0");
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
            if (product.getIsPromotion() && countCurrentProduct > 5) {
                BigDecimal productPriceAsBigDecimal = BigDecimal.valueOf(product.getPrice());
                double finalPrice = productPriceAsBigDecimal.multiply(new BigDecimal(countCurrentProduct)).
                        subtract(new BigDecimal(countCurrentProduct).
                                multiply(productPriceAsBigDecimal).
                                multiply(new BigDecimal("0.1"))).
                        setScale(3, RoundingMode.CEILING).
                        doubleValue();
                ProductDTO productDTO = new ProductDTO.Builder().
                        withProduct(product).
                        withCount(countCurrentProduct).
                        withFinalPrice(finalPrice).
                        build();
                productDTOList.add(productDTO);
            } else {
                BigDecimal productPriceAsBigDecimal = BigDecimal.valueOf(product.getPrice());
                double finalPrice = productPriceAsBigDecimal.multiply(new BigDecimal(countCurrentProduct)).
                        setScale(3, RoundingMode.CEILING).
                        doubleValue();
                ProductDTO productDTO = new ProductDTO.Builder().
                        withProduct(product).
                        withCount(countCurrentProduct).
                        withFinalPrice(finalPrice).
                        build();
                productDTOList.add(productDTO);
            }
        }
        return productDTOList;
    }

}
