package com.learn.product_service.controller;

import com.learn.product_service.dto.OrderRequest;
import com.learn.product_service.exception.OrderQuantityException;
import com.learn.product_service.exception.ProductNotFoundException;
import com.learn.product_service.model.Order;
import com.learn.product_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@Valid @RequestBody OrderRequest req) throws OrderQuantityException, ProductNotFoundException {
        Order order=orderService.placeOrder(req.getProductId(), req.getQuantity());
        return ResponseEntity.status(201).body(order);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/product/{productId}")
    public List<Order> getByProduct(@PathVariable Long productId){
        return orderService.getOrdersByProduct(productId);
    }
}
