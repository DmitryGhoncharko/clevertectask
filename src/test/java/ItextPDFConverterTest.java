import com.example.mytask.dto.CheckDTO;
import com.example.mytask.dto.ProductDTO;
import com.example.mytask.model.DiscountCard;
import com.example.mytask.model.Product;
import com.example.mytask.pdf.PDFConverter;
import com.example.mytask.pdf.PDFConverterFactory;
import com.example.mytask.pdf.SimplePDFConverterFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class ItextPDFConverterTest {

    @Test
    public void testPDFCreate() {
        PDFConverterFactory pdfConverterFactory = new SimplePDFConverterFactory();
        PDFConverter pdfConverter = pdfConverterFactory.createPDFConverter();
        CheckDTO checkDTO = initCheckDTO();
        Assertions.assertTrue(pdfConverter.convertToPDFAndSave(checkDTO)>0);
    }

    public CheckDTO initCheckDTO() {
        return new CheckDTO.Builder().
                withProductsDTO(Collections.
                        singletonList(new ProductDTO.
                                Builder().
                                withProduct(new Product.
                                        Builder().
                                        withName("Bread").
                                        withId(1L).
                                        withIsPromotion(true).
                                        withPrice(22.2).
                                        build())
                                .withCount(3).
                                withFinalPrice(66.6).
                                build())).
                withDiscountCard(new DiscountCard.
                        Builder().
                        withId(1L).
                        withDiscount(20.0).
                        build()).
                withTotalPrice(53.28).
                build();
    }
}
