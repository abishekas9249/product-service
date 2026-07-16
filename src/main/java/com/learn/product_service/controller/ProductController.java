package com.learn.product_service.controller;

import com.learn.product_service.config.ApplicationProperties;
import com.learn.product_service.dto.PageResponse;
import com.learn.product_service.dto.ProductRequest;
import com.learn.product_service.model.Product;
import com.learn.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public PageResponse<Product> getAll(
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc")  String direction) {

        // cap size to prevent abuse
        int safeSize = Math.min(size, 100);
        return PageResponse.of(
                productService.getAllProductsPaged(
                        page, safeSize, sortBy, direction));
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
    public Page<Product> getByCategory(@PathVariable("category") String category,@RequestParam(defaultValue = "0")    int page,
                                       @RequestParam(defaultValue = "10")   int size){
        return productService.getByCategoryPaged(category,page,size);
    }

    @GetMapping("/count")
    public Map<String,Long> getCount(){
        return Map.of("total",productService.getAllProductsPaged(0,5,"name","asc").getTotalElements());
    }

    @GetMapping("/search")
    public PageResponse<Product> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        return PageResponse.of(
                productService.searchProductsPaged(
                        keyword, page, size));
    }


    @GetMapping("/price-range")
    public Page<Product> byPriceRange(
            @RequestParam Double min,
            @RequestParam Double max,@RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getByPriceRange(min, max,page,size);
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
