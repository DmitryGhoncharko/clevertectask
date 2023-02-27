package entity;

import java.util.Arrays;
import java.util.Objects;


public class DataForValidatorTestEnity {
    private final String[] productsId;
    private final String[] countProductsOnEachId;
    private final String discountCardId;

    public DataForValidatorTestEnity(String[] productsId, String[] countProductsOnEachId, String discountCardId) {
        this.productsId = productsId;
        this.countProductsOnEachId = countProductsOnEachId;
        this.discountCardId = discountCardId;
    }

    public String[] getProductsId() {
        return productsId;
    }

    public String[] getCountProductsOnEachId() {
        return countProductsOnEachId;
    }

    public String getDiscountCardId() {
        return discountCardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataForValidatorTestEnity that = (DataForValidatorTestEnity) o;
        return Arrays.equals(productsId, that.productsId) && Arrays.equals(countProductsOnEachId, that.countProductsOnEachId) && Objects.equals(discountCardId, that.discountCardId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(discountCardId);
        result = 31 * result + Arrays.hashCode(productsId);
        result = 31 * result + Arrays.hashCode(countProductsOnEachId);
        return result;
    }

    @Override
    public String toString() {
        return "DataForValidatorTest{" +
                "productsId=" + Arrays.toString(productsId) +
                ", countProductsOnEachId=" + Arrays.toString(countProductsOnEachId) +
                ", discountCardId='" + discountCardId + '\'' +
                '}';
    }
}
