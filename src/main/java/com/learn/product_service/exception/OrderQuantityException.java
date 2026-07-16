package com.learn.product_service.exception;

public class OrderQuantityException extends RuntimeException {
    public OrderQuantityException(String s) {
        super(s);
    }
}
