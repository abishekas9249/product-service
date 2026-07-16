package com.learn.product_service.health;

import com.learn.product_service.repository.ProductRepository;
import com.learn.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ProductHealthIndicator
        implements HealthIndicator {

    private final ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    public ProductHealthIndicator(
            ProductService productService) {
        this.productService = productService;

    }

    @Override
    public Health health() {
        try {
            int count = productRepository.findAll().size();

            if (count > 0) {
                return Health.up()
                        .withDetail("products", count)
                        .withDetail("status", "data available")
                        .build();
            } else {
                return Health.down()
                        .withDetail("products", 0)
                        .withDetail("reason", "no products found")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withException(e)
                    .build();
        }
    }
}
