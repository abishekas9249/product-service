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
import java.util.Map;
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

    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest req) {
        return productService.updateProduct(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id")Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category){
        return productService.getByCategory(category);
    }

    @GetMapping("/count")
    public Map<String,Integer> getCount(){
        return Map.of("total",productService.getAllProducts().size());
    }

    @GetMapping("/search")
    public List<Product> search(
            @RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    @GetMapping("/price-range")
    public List<Product> byPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return productService.getByPriceRange(min, max);
    }

    @GetMapping("/stats")
    public List<Object[]> stats() {
        return productService.getCategoryStats();
    }

    @PatchMapping("/price-increase/{category}")
    public int priceIncrease(
            @PathVariable String category,
            @RequestParam Double factor) {
        return productService
                .applyPriceIncrease(category, factor);
    }

}
