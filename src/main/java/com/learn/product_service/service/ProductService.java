package com.learn.product_service.service;

import com.learn.product_service.dto.ProductRequest;
import com.learn.product_service.exception.ProductNotFoundException;
import com.learn.product_service.model.Product;
import com.learn.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public Product getByProductId(long id){
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException(id));
    }
    public Product createProduct(ProductRequest pr){
        if (productRepository.existsByNameIgnoreCase(pr.getName())) {
            throw new RuntimeException(
                    "Product already exists: " + pr.getName());
        }
        Product p = new Product();
        p.setName(pr.getName());
        p.setPrice(pr.getPrice());
        p.setCategory(pr.getCategory());
        return productRepository.save(p);
    }
    public void delete(long id){
        Product p=getByProductId(id);
        productRepository.delete(p);
    }
    public List<Product> getByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    public Product updateProduct(
            Long id, ProductRequest req) {
        Product p = getByProductId(id);
        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setCategory(req.getCategory());
        return productRepository.save(p);
    }
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByName(keyword);
    }

    public List<Product> getByPriceRange(
            Double min, Double max) {
        return productRepository.findByPriceBetween(min, max);
    }

    public List<Object[]> getCategoryStats() {
        return productRepository.getCategoryStats();
    }

    public int applyPriceIncrease(
            String category, Double factor) {
        return productRepository
                .applyPriceIncrease(category, factor);
    }
}
