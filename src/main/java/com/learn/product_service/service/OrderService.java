package com.learn.product_service.service;

import com.learn.product_service.exception.OrderQuantityException;
import com.learn.product_service.exception.ProductNotFoundException;
import com.learn.product_service.model.Order;
import com.learn.product_service.model.Product;
import com.learn.product_service.repository.OrderRepository;
import com.learn.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional(rollbackFor = Exception.class)
    public Order placeOrder(Long productId, Integer quantity) throws OrderQuantityException, ProductNotFoundException {

        //step 1 - validate product
        Product product=productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException(productId));

        //step 2 - calculate price
        Double total=product.getPrice()*quantity;

        //step 3 - save order
        Order order=new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(total);
        Order saved=orderRepository.save(order);

        //step 4 - stimulate stock update
        //(throws if quantity>10 to demo rollback)
        if(quantity>10){
            throw new OrderQuantityException("Insufficient Stock for Quantity : "+quantity);
        }

        saved.setStatus("CONFIRMED");
        return orderRepository.save(saved);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByProduct(Long productId){
        return orderRepository.findByProductId(productId);
    }
}
