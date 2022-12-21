package com.example.mytask.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class DiscountCard {
    private final Long id;
    private final Double discount;

    private DiscountCard(Builder builder) {
        this.id = builder.id;
        this.discount = builder.discount;
    }

    public static class Builder {
        private Long id;
        private Double discount;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withDiscount(Double discount) {
            this.discount = discount;
            return this;
        }

        public DiscountCard build() {
            return new DiscountCard(this);
        }
    }
}
