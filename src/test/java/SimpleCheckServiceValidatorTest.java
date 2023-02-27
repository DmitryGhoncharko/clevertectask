import com.example.mytask.validator.CheckServiceValidator;
import com.example.mytask.validator.SimpleCheckServiceValidatorFactory;
import entity.DataForValidatorTestEnity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleCheckServiceValidatorTest {
    private CheckServiceValidator checkServiceValidator = new SimpleCheckServiceValidatorFactory().createValidator();


    public static List<DataForValidatorTestEnity> getDataForIsNotValidTest() {
        return readDataFromFile("src/test/resources/simpleCheckServiceValidatorInvalidData.csv");
    }

    public static List<DataForValidatorTestEnity> getDataForIsValidTest() {
        return readDataFromFile("src/test/resources/simpleCheckServiceValiatorValidData.csv");
    }

    private static List<DataForValidatorTestEnity> readDataFromFile(String filePath) {
        List<DataForValidatorTestEnity> dataForValidatorTestList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] lineToArrays = line.split(",");
                for (int i = 0; i < lineToArrays.length - 2; i++) {
                    dataForValidatorTestList.add(new DataForValidatorTestEnity(lineToArrays[i].split(" "), lineToArrays[i + 1].split(" "), lineToArrays[i + 2]));
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataForValidatorTestList;
    }

    @ParameterizedTest
    @MethodSource("getDataForIsNotValidTest")
    public void validateTestDataIsNotValid(DataForValidatorTestEnity data) {
        boolean dataIsValid = checkServiceValidator.validate(data.getProductsId(), data.getCountProductsOnEachId(), data.getDiscountCardId());
        Assertions.assertFalse(dataIsValid);
    }

    @ParameterizedTest
    @MethodSource("getDataForIsValidTest")
    public void validateTestDataIsValid(DataForValidatorTestEnity data) {
        boolean dataIsValid = checkServiceValidator.validate(data.getProductsId(), data.getCountProductsOnEachId(), data.getDiscountCardId());
        Assertions.assertTrue(dataIsValid);
    }
}
