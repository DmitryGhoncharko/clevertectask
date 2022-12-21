import com.example.mytask.validator.CheckServiceValidator;
import com.example.mytask.validator.SimpleCheckServiceValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleCheckServiceValidatorTest {
    private CheckServiceValidator checkServiceValidator = new SimpleCheckServiceValidatorFactory().createValidator();

    @Test
    public void validateTestDataIsNotValid() {
        boolean dataIsValid = checkServiceValidator.validate(null, null, null);
        Assertions.assertFalse(dataIsValid);
    }

    @Test
    public void validateTestDataIsValid() {
        boolean dataIsValid = checkServiceValidator.validate(new String[]{}, new String[]{}, "");
        Assertions.assertTrue(dataIsValid);
    }
}
