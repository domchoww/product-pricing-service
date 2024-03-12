package com.inpost.productpricingservice.product;

import com.inpost.productpricingservice.exception.ProductsNotFoundException;
import com.inpost.productpricingservice.model.CollectProductRequest;
import com.inpost.productpricingservice.model.CollectProduct;
import com.inpost.productpricingservice.model.Product;
import com.inpost.productpricingservice.model.ProductAllocation;
import com.inpost.productpricingservice.pricing.PricingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PricingService pricingService;

    public ProductService(ProductRepository productRepository, PricingService pricingService) {
        this.productRepository = productRepository;
        this.pricingService = pricingService;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<CollectProduct> collect(CollectProductRequest collectProductRequest) throws ProductsNotFoundException {
        List<UUID> ids = collectProductRequest.productAllocations().stream().map(ProductAllocation::productId).toList();
        Map<UUID, Product> idToPersistedProduct = productRepository.findAllById(ids).stream().collect(toMap(Product::id, p -> p));
        List<ProductAllocation> productAllocations = collectProductRequest.productAllocations();
        return pricingService.getProductPricing(idToPersistedProduct, productAllocations);
    }
}
