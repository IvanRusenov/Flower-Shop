package com.ivan.flower_shop.orders.controllers;

import com.ivan.flower_shop.orders.models.entities.OrderItem;
import com.ivan.flower_shop.orders.services.OrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItem> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    @GetMapping("/{orderItemId}")
    public OrderItem getOrderItemById(@PathVariable Long orderItemId) {
        return orderItemService.getOrderItemById(orderItemId);
    }

    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemService.saveOrderItem(orderItem);
    }

    @PutMapping("/{orderItemId}")
    public OrderItem updateOrderItem(@PathVariable Long orderItemId, @RequestBody OrderItem orderItemDetails) {
        OrderItem orderItem = orderItemService.getOrderItemById(orderItemId);
        if (orderItem != null) {
            orderItem.setBouquetId(orderItemDetails.getBouquetId());
            orderItem.setQuantity(orderItemDetails.getQuantity());
            orderItem.setUnitPrice(orderItemDetails.getUnitPrice());
            orderItem.setTotalPrice(orderItemDetails.getTotalPrice());
            return orderItemService.saveOrderItem(orderItem);
        }
        return null;
    }

    @DeleteMapping("/{orderItemId}")
    public void deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
    }
}
