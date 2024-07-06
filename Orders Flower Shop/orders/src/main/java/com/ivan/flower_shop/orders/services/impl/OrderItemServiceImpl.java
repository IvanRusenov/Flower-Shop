package com.ivan.flower_shop.orders.services.impl;

import com.ivan.flower_shop.orders.models.entities.OrderItem;
import com.ivan.flower_shop.orders.repositories.OrderItemRepository;
import com.ivan.flower_shop.orders.services.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem getOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElse(null);
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
}

