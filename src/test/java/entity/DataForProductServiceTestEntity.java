package entity;

import java.util.Objects;

public class DataForProductServiceTestEntity {
    private final Long productId;
    private final String productName;
    private final Double productPrice;
    private final boolean isPromotion;

    public DataForProductServiceTestEntity(Long productId, String productName, Double productPrice, boolean isPromotion) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.isPromotion = isPromotion;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataForProductServiceTestEntity that = (DataForProductServiceTestEntity) o;

        if (isPromotion != that.isPromotion) return false;
        if (!Objects.equals(productId, that.productId)) return false;
        if (!Objects.equals(productName, that.productName)) return false;
        return Objects.equals(productPrice, that.productPrice);
    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (productPrice != null ? productPrice.hashCode() : 0);
        result = 31 * result + (isPromotion ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataForProductServiceTestEntity{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", isPromotion=" + isPromotion +
                '}';
    }
}
