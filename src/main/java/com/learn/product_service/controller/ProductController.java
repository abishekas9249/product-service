package com.learn.product_service.controller;

import com.learn.product_service.config.ApplicationProperties;
import com.learn.product_service.dto.ProductRequest;
import com.learn.product_service.model.Product;
import com.learn.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ApplicationProperties appProperties;
    private final ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts().stream()
                .limit(appProperties.getMaxPageSize()).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable("id") Long id) {
        return productService.getByProductId(id);
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequest pr) {
        Product product=productService.createProduct(pr);
        return ResponseEntity.status(201).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id")Long id){
        productService.delete(id);
        return ResponseEntity.status(200).body("Product"+id+" has been successfully deleted.");
    }
}
