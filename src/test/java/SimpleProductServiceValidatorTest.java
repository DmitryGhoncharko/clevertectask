import com.example.mytask.validator.ProductServiceValidator;
import com.example.mytask.validator.SimpleProductServiceValidatorFactory;
import entity.DataForProductServiceTestEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleProductServiceValidatorTest {
    private final ProductServiceValidator productServiceValidator = new SimpleProductServiceValidatorFactory().create();

    public static List<DataForProductServiceTestEntity> getDataForIsNotValidTest() {
        return readDataFromFile("src/test/resources/simpleProductServiceValidatorInvalidData.csv");
    }

    public static List<DataForProductServiceTestEntity> getDataForIsValidTest() {
        return readDataFromFile("src/test/resources/simpleProductServiceValidatorValidData.csv");
    }

    private static List<DataForProductServiceTestEntity> readDataFromFile(String filePath) {
        List<DataForProductServiceTestEntity> dataForValidatorTestList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] lineToArrays = line.split(" ");
                dataForValidatorTestList.add(new DataForProductServiceTestEntity(Long.valueOf(lineToArrays[0]), lineToArrays[1], Double.valueOf(lineToArrays[2]), Boolean.parseBoolean(lineToArrays[3])));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataForValidatorTestList;
    }

    @ParameterizedTest
    @MethodSource("getDataForIsNotValidTest")
    public void validateTestDataIsNotValid(DataForProductServiceTestEntity data) {
        boolean dataIsValid = productServiceValidator.validate(String.valueOf(data.getProductId()), data.getProductName(), data.getProductPrice(), data.isPromotion());
        Assertions.assertFalse(dataIsValid);
    }

    @ParameterizedTest
    @MethodSource("getDataForIsValidTest")
    public void validateTestDataIsValid(DataForProductServiceTestEntity data) {
        boolean dataIsValid = productServiceValidator.validate(String.valueOf(data.getProductId()), data.getProductName(), data.getProductPrice(), data.isPromotion());
        Assertions.assertTrue(dataIsValid);
    }
}
