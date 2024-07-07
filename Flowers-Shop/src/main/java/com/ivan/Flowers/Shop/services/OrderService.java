package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.dtos.OrderDTO;

public interface OrderService {

    void createOrder(OrderDTO orderDTO);

    OrderDTO createOrderDTO(long cartId);
}
