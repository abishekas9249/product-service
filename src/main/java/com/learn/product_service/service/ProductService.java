package com.learn.product_service.service;

import com.learn.product_service.dto.ProductRequest;
import com.learn.product_service.exception.ProductNotFoundException;
import com.learn.product_service.model.Product;
import com.learn.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getAllProductsPaged(
            int page, int size,
            String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
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
    public Page<Product> getByCategoryPaged(String category,int page,int size) {
        Pageable pageable=PageRequest.of(page,size,Sort.by("price").descending());
        return productRepository.findByCategory(category,pageable);
    }
    public Product updateProduct(
            Long id, ProductRequest req) {
        Product p = getByProductId(id);
        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setCategory(req.getCategory());
        return productRepository.save(p);
    }
    public Page<Product> searchProductsPaged(String keyword,int page,int size) {

        Pageable pageable=PageRequest.of(page,size,Sort.by("price").ascending());
        return productRepository.findByNameContainingIgnoreCase(keyword,pageable);
    }

    public Page<Product> getByPriceRange(
            Double min, Double max,int page,int size) {
        Pageable pageable=PageRequest.of(page,size,Sort.by("price").ascending());
        return productRepository.findByPriceBetween(min, max,pageable);
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
