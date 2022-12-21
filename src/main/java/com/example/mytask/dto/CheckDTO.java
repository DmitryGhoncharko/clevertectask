package com.example.mytask.dto;

import com.example.mytask.model.DiscountCard;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class CheckDTO {
    private final List<ProductDTO> products;
    private final DiscountCard discountCard;
    private final double totalPrice;

    private CheckDTO(Builder builder) {
        products = builder.products;
        discountCard = builder.discountCard;
        totalPrice = builder.totalPrice;
    }

    public static class Builder {
        private List<ProductDTO> products;
        private DiscountCard discountCard;

        private double totalPrice;

        public Builder withProductsDTO(List<ProductDTO> products) {
            this.products = products;
            return this;
        }

        public Builder withDiscountCard(DiscountCard discountCard) {
            this.discountCard = discountCard;
            return this;
        }

        public Builder withTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public CheckDTO build() {
            return new CheckDTO(this);
        }
    }
}
