package com.inpost.productpricingservice.model;


import java.math.BigDecimal;

public record PromotionRange(
        Integer countFrom,
        Integer countTo,
        BigDecimal discountPercentage) {
}
