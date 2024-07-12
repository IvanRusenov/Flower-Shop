package com.ivan.flower_shop.orders.models.dtos;

import com.ivan.flower_shop.orders.enums.StatusType;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private long id;
    private Long userId;
    private LocalDateTime orderDateTime;
    private double totalAmount;
    private StatusType status;
    private String shippingAddress;
    private List<OrderItemDTO> items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
