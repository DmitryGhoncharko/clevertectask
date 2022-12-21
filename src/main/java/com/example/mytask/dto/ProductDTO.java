package com.example.mytask.dto;

import com.example.mytask.model.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ProductDTO {
    private final Product product;
    private final int count;
    private final double finalPrise;

    private ProductDTO(Builder builder) {
        product = builder.product;
        count = builder.count;
        finalPrise = builder.finalPrice;
    }

    public static class Builder {
        private Product product;
        private int count;

        private double finalPrice;

        public Builder withProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder withCount(int count) {
            this.count = count;
            return this;
        }

        public Builder withFinalPrice(double finalPrice) {
            this.finalPrice = finalPrice;
            return this;
        }

        public ProductDTO build() {
            return new ProductDTO(this);
        }
    }
}
