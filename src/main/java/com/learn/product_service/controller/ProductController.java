package com.learn.product_service.controller;

import com.learn.product_service.dto.ProductRequest;
import com.learn.product_service.exception.ProductNotFoundException;
import com.learn.product_service.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("api/products")
@RestController
public class ProductController {
    private List<Product> products = new ArrayList<>(
            List.of(new Product(1L, "Laptop", 750000.0, "Electronics"),
                    new Product(2L, "Phone", 25000.0, "Devices"),
                    new Product(3L, "Desk", 12000.0, "Furniture"))
    );

    @GetMapping
    public List<Product> getAll() {
        return products;
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable("id") Long id) {
        return products.stream().filter(p -> p.getId()
        .equals(id)).findFirst()
        .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequest pr) {
        Product product=new Product((long)(products.size()+1),pr.getName(),pr.getPrice(),pr.getCategory());
        products.add(product);
        return ResponseEntity.status(201).body(product);
    }
}
