import com.example.mytask.validator.DiscountCardServiceValidator;
import com.example.mytask.validator.SimpleDiscountCardServiceValidatorFactory;
import entity.DataForDiscountCardValidatorTestEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleDiscountCardServiceValidatorTest {
    private final DiscountCardServiceValidator productServiceValidator = new SimpleDiscountCardServiceValidatorFactory().createValidator();

    public static List<DataForDiscountCardValidatorTestEntity> getDataForIsNotValidTest() {
        return readDataFromFile("src/test/resources/discountCardServiceValidatorInvalidData.csv");
    }

    public static List<DataForDiscountCardValidatorTestEntity> getDataForIsValidTest() {
        return readDataFromFile("src/test/resources/discountCardServiceValidatorValidData.csv");
    }

    private static List<DataForDiscountCardValidatorTestEntity> readDataFromFile(String filePath) {
        List<DataForDiscountCardValidatorTestEntity> dataForValidatorTestList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] lineToArrays = line.split(" ");
                dataForValidatorTestList.add(new DataForDiscountCardValidatorTestEntity(Long.valueOf(lineToArrays[0]), Double.parseDouble(lineToArrays[1])));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataForValidatorTestList;
    }

    @ParameterizedTest
    @MethodSource("getDataForIsNotValidTest")
    public void validateTestDataIsNotValid(DataForDiscountCardValidatorTestEntity data) {
        boolean dataIsValid = productServiceValidator.validate(String.valueOf(data.getId()), data.getDiscount());
        Assertions.assertFalse(dataIsValid);
    }

    @ParameterizedTest
    @MethodSource("getDataForIsValidTest")
    public void validateTestDataIsValid(DataForDiscountCardValidatorTestEntity data) {
        boolean dataIsValid = productServiceValidator.validate(String.valueOf(data.getId()), data.getDiscount());
        Assertions.assertTrue(dataIsValid);
    }
}
