package com.inpost.productpricingservice.exception;

import java.util.List;
import java.util.UUID;

public class ProductsNotFoundException extends Exception {
    public ProductsNotFoundException(List<UUID> missingProductsIds) {
        super(String.format("Products not found: %s", missingProductsIds.toString()));
    }
}
