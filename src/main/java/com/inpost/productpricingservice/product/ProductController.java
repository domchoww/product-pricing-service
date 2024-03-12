package com.inpost.productpricingservice.product;

import com.inpost.productpricingservice.exception.ProductsNotFoundException;
import com.inpost.productpricingservice.model.CollectProductRequest;
import com.inpost.productpricingservice.model.CollectProduct;
import com.inpost.productpricingservice.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products")
    private ResponseEntity<List<Product>> getProducts() {
        return ok(productService.getProducts());
    }

    @PostMapping("/products/collect")
    private ResponseEntity<List<CollectProduct>> collectProducts(@RequestBody CollectProductRequest collectProductRequest) throws ProductsNotFoundException {
        return ok(productService.collect(collectProductRequest));
    }
}
