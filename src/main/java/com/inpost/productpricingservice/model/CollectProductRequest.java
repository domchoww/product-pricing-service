package com.inpost.productpricingservice.model;

import java.util.List;

public record CollectProductRequest(List<ProductAllocation> productAllocations) {
}
