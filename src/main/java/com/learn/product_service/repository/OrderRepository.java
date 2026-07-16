package com.learn.product_service.repository;

import com.learn.product_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByProductId(Long productId);
    List<Order> findByStatus(String status);
}
