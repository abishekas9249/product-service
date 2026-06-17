package com.learn.product_service.service;

import com.learn.product_service.dto.ProductRequest;
import com.learn.product_service.exception.ProductNotFoundException;
import com.learn.product_service.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

public class ProductServiceTest {
    private ProductService productService;
    @BeforeEach
    void setUp(){
        productService=new ProductService();
    }

    @Test
    void getAllProducts_returnNonEmptyList(){
        List<Product> result=productService.getAllProducts();
        assertThat(result).isNotEmpty();
    }

    @Test
    void getProductById_ExistingId_ReturnProduct(){
        Product product=productService.getByProductId(1L);
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Lenovo Laptop");
    }

    @Test
    void getProductById_NonExistingId_throwsException(){
        assertThatThrownBy(()->productService.getByProductId(9L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("9");
    }

    @Test
    void createProduct_addsToList(){
        ProductRequest pr=new ProductRequest("Iphone 16 Pro",75000.0,"Electronics");
        int beforeSize=productService.getAllProducts().size();
        productService.createProduct(pr);

        assertThat(productService.getAllProducts().size()).isEqualTo(beforeSize+1);
    }

    @Test
    void deleteProduct_removesFromList(){
        int beforeSize=productService.getAllProducts().size();
        productService.delete(2L);
        assertThat(productService.getAllProducts().size()).isEqualTo(beforeSize-1);
    }
}
