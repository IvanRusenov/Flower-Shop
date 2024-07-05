package com.ivan.flower_shop.orders.models.dtos;

import com.ivan.flower_shop.orders.enums.StatusType;

import java.time.LocalDateTime;

public class CreateOrderDTO {
    private LocalDateTime orderDateTime;
    private double totalAmount;
    private StatusType status;
    private String shippingAddress;

}
