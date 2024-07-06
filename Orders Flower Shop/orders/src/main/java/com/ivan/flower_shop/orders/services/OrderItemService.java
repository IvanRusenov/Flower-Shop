package com.ivan.flower_shop.orders.services;

import com.ivan.flower_shop.orders.models.entities.OrderItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> getAllOrderItems();
    OrderItem getOrderItemById(Long orderItemId);
    OrderItem saveOrderItem(OrderItem orderItem);
    void deleteOrderItem(Long orderItemId);

}
