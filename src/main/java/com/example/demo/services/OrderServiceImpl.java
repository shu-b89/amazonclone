package com.example.demo.services;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.repoository.OrderItemRepository;
import com.example.demo.repoository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(Order order, List<OrderItem> items) {
        // Generate SM-XXXXXXXX order ID
        String orderId = "SM-" + String.valueOf(System.currentTimeMillis()).substring(5);
        order.setOrderId(orderId);

        // Save order first
        Order savedOrder = orderRepository.save(order);

        // Save each order item
        for (OrderItem item : items) {
            item.setOrderId(savedOrder.getId());
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
