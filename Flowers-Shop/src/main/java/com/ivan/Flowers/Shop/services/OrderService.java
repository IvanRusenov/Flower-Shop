package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.dtos.CreateOrderDTO;
import com.ivan.Flowers.Shop.models.dtos.OrderDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {

    void createOrder(CreateOrderDTO orderDTO);

    CreateOrderDTO createOrderDTO(long cartId);

    List<OrderDTO> getAllOrdersByUser(UserDetails userDetails);
}
