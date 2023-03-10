package entity;

import com.example.mytask.model.DiscountCard;
import com.example.mytask.model.Product;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CheckByProductsIdsAndDiscountCardTestEntity {
    private final String[] productsId;
    private final List<Product> products;
    private final String[] countProductsOnEachId;
    private final String discountCardId;
    private final Optional<DiscountCard> discountCard;

    public CheckByProductsIdsAndDiscountCardTestEntity(String[] productsId, List<Product> products, String[] countProductsOnEachId, String discountCardId, Optional<DiscountCard> discountCard) {
        this.productsId = productsId;
        this.products = products;
        this.countProductsOnEachId = countProductsOnEachId;
        this.discountCardId = discountCardId;
        this.discountCard = discountCard;
    }

    public String[] getProductsId() {
        return productsId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getDiscountCardId() {
        return discountCardId;
    }

    public String[] getCountProductsOnEachId() {
        return countProductsOnEachId;
    }

    public Optional<DiscountCard> getDiscountCard() {
        return discountCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckByProductsIdsAndDiscountCardTestEntity that = (CheckByProductsIdsAndDiscountCardTestEntity) o;
        return Arrays.equals(productsId, that.productsId) && Objects.equals(products, that.products) && Arrays.equals(countProductsOnEachId, that.countProductsOnEachId) && Objects.equals(discountCardId, that.discountCardId) && Objects.equals(discountCard, that.discountCard);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(products, discountCardId, discountCard);
        result = 31 * result + Arrays.hashCode(productsId);
        result = 31 * result + Arrays.hashCode(countProductsOnEachId);
        return result;
    }

    @Override
    public String toString() {
        return "CheckByProductsIdsAndDiscountCardTestEntity{" +
                "productsId=" + Arrays.toString(productsId) +
                ", products=" + products +
                ", countProductsOnEachId=" + Arrays.toString(countProductsOnEachId) +
                ", discountCardId='" + discountCardId + '\'' +
                ", discountCard=" + discountCard +
                '}';
    }
}
