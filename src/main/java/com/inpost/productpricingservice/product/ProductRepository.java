package com.inpost.productpricingservice.product;

import com.inpost.productpricingservice.exception.ProductsNotFoundException;
import com.inpost.productpricingservice.model.MonetaryValue;
import com.inpost.productpricingservice.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository {

    private final List<Product> persistedProducts = List.of(
            new Product(UUID.fromString("00000000-0000-0000-0000-000000000001"), "product_name_1", new MonetaryValue(new BigDecimal("1.1"))),
            new Product(UUID.fromString("00000000-0000-0000-0000-000000000002"), "product_name_2", new MonetaryValue(new BigDecimal("20.0"))),
            new Product(UUID.fromString("00000000-0000-0000-0000-000000000003"), "product_name_3", new MonetaryValue(new BigDecimal("11.03"))),
            new Product(UUID.fromString("00000000-0000-0000-0000-000000000004"), "product_name_4", new MonetaryValue(new BigDecimal("15.23"))),
            new Product(UUID.fromString("00000000-0000-0000-0000-000000000005"), "product_name_5", new MonetaryValue(new BigDecimal("0.01")))
    );

    public List<Product> findAll() {
        return persistedProducts;
    }

    public List<Product> findAllById(List<UUID> productIds) throws ProductsNotFoundException {
        List<Product> productsFound = persistedProducts.stream().filter(product -> productIds.contains(product.id())).toList();
        List<UUID> idsNotFound = new ArrayList<>(productIds);
        idsNotFound.removeAll(productsFound.stream().map(Product::id).toList());
        if (!idsNotFound.isEmpty()) throw new ProductsNotFoundException(idsNotFound);
        return persistedProducts.stream().filter(product -> productIds.contains(product.id())).toList();
    }
}
