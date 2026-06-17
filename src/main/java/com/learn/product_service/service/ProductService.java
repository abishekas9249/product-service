package com.learn.product_service.service;

import com.learn.product_service.dto.ProductRequest;
import com.learn.product_service.exception.ProductNotFoundException;
import com.learn.product_service.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final List<Product> products = new ArrayList<>(List.of(
            new Product(1L, "Lenovo Laptop", 75000.0, "Electronics"),
            new Product(2L, "Puma", 5000.0, "Shoes"),
            new Product(3L, "Acer Laptop", 550000.0, "Electronics"),
            new Product(4L, "Realme Narzo 30A Mobile Phone", 25000.0, "Devices"),
            new Product(5L, "Desk", 12000.0, "Furniture")));
    public List<Product> getAllProducts(){
        return Collections.unmodifiableList(products);
    }
    public Product getByProductId(long id){
        return products.stream().filter(p->p.getId().equals(id))
                .findFirst().orElseThrow(()->new ProductNotFoundException(id));
    }
    public Product createProduct(ProductRequest pr){
        Product product=new Product((long)products.size()+1,pr.getName(),pr.getPrice(),pr.getCategory());
        products.add(product);
        return product;
    }
    public void delete(long id){
        Product p=getByProductId(id);
        products.remove(p);
    }
}
