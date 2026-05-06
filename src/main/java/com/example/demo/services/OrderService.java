package com.example.demo.services;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order, List<OrderItem> items);
    List<Order> getOrdersByUser(Long userId);
    Order getOrderById(Long id);
    List<OrderItem> getOrderItems(Long orderId);
}