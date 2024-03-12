package com.inpost.productpricingservice.model;

import java.util.UUID;

public record Product(UUID id, String name, MonetaryValue basePrice) {}
