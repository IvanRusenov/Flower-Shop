package com.ivan.flower_shop.orders.services.impl;

import com.ivan.flower_shop.orders.models.entities.Order;
import com.ivan.flower_shop.orders.repositories.OrderRepository;
import com.ivan.flower_shop.orders.services.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }


}
