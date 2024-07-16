package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.enums.StatusType;
import com.ivan.Flowers.Shop.models.dtos.OrderDetailsDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {

    void createOrder(long cartId);

    List<OrderDetailsDTO> getAllOrdersFromUser(UserDetails userDetails);

    OrderDetailsDTO getLastOrderByUser(UserDetails userDetails);

    void changOrderStatus(long orderId, StatusType newStatus);

    List<OrderDetailsDTO> getAllPendingOrders();

    List<OrderDetailsDTO> getAllOrdersDesc();

    void delete(long id);

//    void edit(long id);

    OrderDetailsDTO getOrder(long id);

    void edit(OrderDetailsDTO orderDetailsDTO);
}
