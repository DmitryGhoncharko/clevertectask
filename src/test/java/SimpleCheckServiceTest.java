import com.example.mytask.dao.DiscountCardDao;
import com.example.mytask.dao.ProductDao;
import com.example.mytask.dto.CheckDTO;
import com.example.mytask.dto.ProductDTO;
import com.example.mytask.exception.DaoException;
import com.example.mytask.exception.ServiceException;
import com.example.mytask.exception.ValidationFailedException;
import com.example.mytask.model.DiscountCard;
import com.example.mytask.model.Product;
import com.example.mytask.service.CheckService;
import com.example.mytask.service.SimpleCheckService;
import com.example.mytask.validator.SimpleCheckServiceValidator;
import entity.CheckByProductsIdsAndDiscountCardTestEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SimpleCheckServiceTest {
    private ProductDao productDao = Mockito.mock(ProductDao.class);
    private DiscountCardDao discountCardDao = Mockito.mock(DiscountCardDao.class);
    private CheckService checkService = new SimpleCheckService(productDao, discountCardDao, new SimpleCheckServiceValidator());
    private static List<Product> products = new ArrayList<>();
    private static List<ProductDTO> productDTOValidList = new ArrayList<>();


    public static List<CheckByProductsIdsAndDiscountCardTestEntity> getDataForgetCheckByProductsIdsAndDiscountCardIdTestWithoutDiscountCardTest() {
        return readDataFromFile("src/test/resources/getCheckByProductsIdsAndDiscountCardIdTestWithoutDiscountCardData.csv");
    }

    private static List<CheckByProductsIdsAndDiscountCardTestEntity> readDataFromFile(String filePath) {
        List<CheckByProductsIdsAndDiscountCardTestEntity> dataForValidatorTestList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] lineToArrays = line.split(",");
                for (int i = 0; i < lineToArrays.length - 4; i++) {
                    dataForValidatorTestList.add(new CheckByProductsIdsAndDiscountCardTestEntity(
                            lineToArrays[i].
                                    split(" "),
                            Collections.
                                    singletonList(new Product.
                                            Builder().
                                            withId(Long.parseLong(lineToArrays[i + 1].
                                                    split(" ")[0])).
                                            withName(lineToArrays[i + 1].
                                                    split(" ")[1]).
                                            withPrice(Double.parseDouble(lineToArrays[i + 1].
                                                    split(" ")[2])).
                                            withIsPromotion(Boolean.
                                                    parseBoolean(lineToArrays[i + 1].
                                                            split(" ")[3])).
                                            build()),
                            lineToArrays[i + 2].
                                    split(" "),
                            lineToArrays[i + 3],
                            Optional.of(new DiscountCard.
                                    Builder().
                                    withId(Long.parseLong(lineToArrays[i + 4].
                                            split(" ")[0])).
                                    withDiscount(Double.parseDouble(lineToArrays[i + 4].
                                            split(" ")[1])).
                                    build())));
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataForValidatorTestList;
    }

    @ParameterizedTest
    @MethodSource("getDataForgetCheckByProductsIdsAndDiscountCardIdTestWithoutDiscountCardTest")
    public void getCheckByProductsIdsAndDiscountCardIdTestWithoutDiscountCard(CheckByProductsIdsAndDiscountCardTestEntity data) throws DaoException, ServiceException, ValidationFailedException {
        Mockito.when(productDao.getProductsById(data.getProductsId())).thenReturn(data.getProducts());
        Mockito.when(discountCardDao.getCardById(data.getDiscountCardId())).thenReturn(data.getDiscountCard());
        CheckDTO checkDTO = checkService.getCheckByProductsIdsAndDiscountCardId(data.getProductsId(), data.getCountProductsOnEachId(), data.getDiscountCardId());
        Assertions.assertEquals(checkDTO, checkDTO);
    }

    @ParameterizedTest
    @MethodSource("getDataForgetCheckByProductsIdsAndDiscountCardIdTestWithoutDiscountCardTest")
    public void getCheckByProductsIdsAndDiscountCardIdTestWithDiscountCard(CheckByProductsIdsAndDiscountCardTestEntity data) throws DaoException, ServiceException, ValidationFailedException {
        Mockito.when(productDao.getProductsById(data.getProductsId())).thenReturn(data.getProducts());
        Mockito.when(discountCardDao.getCardById(data.getDiscountCardId())).thenReturn(data.getDiscountCard());
        CheckDTO checkDTO = checkService.getCheckByProductsIdsAndDiscountCardId(data.getProductsId(), data.getCountProductsOnEachId(), data.getDiscountCardId());
        Assertions.assertEquals(checkDTO, checkDTO);
    }

    @Test
    public void getCheckByProductsIdsAndDiscountCardIdTestWithDiscountCardValidationFailedExceptionThrows() {
        CheckDTO checkDTO = null;
        try {
            checkDTO = checkService.getCheckByProductsIdsAndDiscountCardId(null, null, null);
            Assertions.fail("Expected ValidationFailedException");
        } catch (ServiceException | ValidationFailedException e) {
            Assertions.assertNotEquals("", e.getMessage());
        }
    }

    @Test
    public void getCheckByProductsIdsAndDiscountCardIdTestWithDiscountCardValidationServiceExceptionThrows() {
        CheckDTO checkDTO = null;
        try {
            Mockito.when(productDao.getProductsById(new String[]{"1"})).thenThrow(new DaoException());
            Mockito.when(discountCardDao.getCardById("1")).thenReturn(Optional.empty());
            checkDTO = checkService.getCheckByProductsIdsAndDiscountCardId(new String[]{"1"}, new String[]{"dsad"}, "dsad");
            Assertions.fail("Expected DaoException");
        } catch (ServiceException | ValidationFailedException e) {
            Assertions.assertNotEquals("", e.getMessage());
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}
