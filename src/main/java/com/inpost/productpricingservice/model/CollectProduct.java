package com.inpost.productpricingservice.model;

import java.util.Optional;
import java.util.UUID;

public record CollectProduct(UUID id, String name, MonetaryValue basePrice, Optional<MonetaryValue> discountedPrice) {}
