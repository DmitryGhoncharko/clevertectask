package entity;

import java.util.Objects;

public class DataForDiscountCardValidatorTestEntity {
    private final Long id;
    private final Double discount;

    public DataForDiscountCardValidatorTestEntity(Long id, Double discount) {
        this.id = id;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public Double getDiscount() {
        return discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataForDiscountCardValidatorTestEntity that = (DataForDiscountCardValidatorTestEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(discount, that.discount);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataForDiscountCardValidatorTestEntity{" + "id=" + id + ", discount=" + discount + '}';
    }
}
