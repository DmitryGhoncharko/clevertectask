package com.example.mytask.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Product {
    private final Long id;
    private final String name;
    private final Double price;

    private final Boolean isPromotion;

    private Product(Builder builder) {
        id = builder.id;
        name = builder.name;
        price = builder.price;
        isPromotion = builder.isPromotion;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Double price;
        private Boolean isPromotion;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPrice(Double price) {
            this.price = price;
            return this;
        }

        public Builder withIsPromotion(Boolean isPromotion) {
            this.isPromotion = isPromotion;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
