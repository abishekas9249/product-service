package com.learn.product_service.exception;

import com.learn.product_service.model.Product;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id){
        super("Product not found with id: "+id);
    }
}
