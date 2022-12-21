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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleCheckServiceTest {
    private ProductDao productDao = Mockito.mock(ProductDao.class);
    private DiscountCardDao discountCardDao = Mockito.mock(DiscountCardDao.class);
    private CheckService checkService = new SimpleCheckService(productDao, discountCardDao, new SimpleCheckServiceValidator());
    private static List<Product> products = new ArrayList<>();
    private static List<ProductDTO> productDTOValidList = new ArrayList<>();
    private static CheckDTO checkDTOValidWithoutDiscountCard;

    private static CheckDTO checkDTOValidWithDiscountCard;
    private static DiscountCard discountCard;

    @BeforeAll
    public static void initProductListData() {
        discountCard = new DiscountCard.Builder().
                withId(1L).
                withDiscount(20.0).
                build();
        products.
                add(new Product.Builder().
                        withName("A").
                        withPrice(1.0).
                        withId(1L).
                        withIsPromotion(true).
                        build());
        productDTOValidList.
                add(new ProductDTO.
                        Builder().
                        withProduct(new Product.
                                Builder().
                                withName("A").
                                withId(1L).
                                withIsPromotion(true).
                                withPrice(1.0).build()).
                        withCount(1).
                        withFinalPrice(1.0).
                        build());
        checkDTOValidWithoutDiscountCard = new CheckDTO.
                Builder().
                withTotalPrice(1.0).
                withDiscountCard(null).
                withProductsDTO(productDTOValidList).
                build();
        checkDTOValidWithDiscountCard = new CheckDTO.
                Builder().
                withTotalPrice(0.8).
                withDiscountCard(discountCard).
                withProductsDTO(productDTOValidList).
                build();
    }

    @Test
    public void getCheckByProductsIdsAndDiscountCardIdTestWithoutDiscountCard() throws DaoException, ServiceException, ValidationFailedException {
        Mockito.when(productDao.getProductsById(new String[]{"1"})).thenReturn(products);
        Mockito.when(discountCardDao.getCardById("1")).thenReturn(Optional.empty());
        CheckDTO checkDTO = checkService.getCheckByProductsIdsAndDiscountCardId(new String[]{"1"}, new String[]{"1"}, "1");
        Assertions.assertEquals(checkDTOValidWithoutDiscountCard, checkDTO);
    }

    @Test
    public void getCheckByProductsIdsAndDiscountCardIdTestWithDiscountCard() throws DaoException, ServiceException, ValidationFailedException {
        Mockito.when(productDao.getProductsById(new String[]{"1"})).thenReturn(products);
        Mockito.when(discountCardDao.getCardById("1")).thenReturn(Optional.of(discountCard));
        CheckDTO checkDTO = checkService.getCheckByProductsIdsAndDiscountCardId(new String[]{"1"}, new String[]{"1"}, "1");
        Assertions.assertEquals(checkDTOValidWithDiscountCard, checkDTO);
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
            Mockito.when(discountCardDao.getCardById("1")).thenReturn(Optional.of(discountCard));
            checkDTO = checkService.getCheckByProductsIdsAndDiscountCardId(new String[]{"1"}, new String[]{"dsad"}, "dsad");
            Assertions.fail("Expected DaoException");
        } catch (ServiceException | ValidationFailedException e) {
            Assertions.assertNotEquals("", e.getMessage());
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}
