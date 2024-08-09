package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.enums.StatusType;
import com.ivan.Flowers.Shop.models.dtos.OrderDetailsDTO;
import com.ivan.Flowers.Shop.models.dtos.OrderItemDetailDTO;
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

    OrderDetailsDTO getOrder(long id);

    void edit(OrderDetailsDTO orderDetailsDTO);

    void deleteItem(Long itemId, long orderId);

    void cancelOrder();

    void save(OrderDetailsDTO orderDetailsDTO);

    OrderItemDetailDTO getOrderItem(long itemId);

    void setCurrentOrder(OrderDetailsDTO orderDetailsDTO);

    OrderDetailsDTO getCurrentOrder();

}
