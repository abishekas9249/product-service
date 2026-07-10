package com.learn.product_service.config;

import com.learn.product_service.model.Product;
import com.learn.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader
        implements ApplicationRunner {

    private final ProductRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                    createProduct("Laptop",75000.0,"Electronics"),
                    createProduct("Phone",25000.0,"Electronics"),
                    createProduct("Desk",12000.0,"Furniture"),
                    createProduct("Chair",8000.0,"Furniture")
            ));
            System.out.println(
                    "Sample products loaded into DB");
        }
    }

    private Product createProduct(
            String name, Double price, String category) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setCategory(category);
        return p;
    }
}
