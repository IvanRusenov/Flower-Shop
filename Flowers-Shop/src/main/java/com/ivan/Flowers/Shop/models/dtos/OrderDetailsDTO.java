package com.ivan.Flowers.Shop.models.dtos;

import com.ivan.Flowers.Shop.enums.StatusType;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailsDTO {

    private long id;
    private LocalDateTime orderDateTime;
    private double totalAmount;
    private StatusType status;
    private String shippingAddress;
    private List<OrderItemDetailDTO> items;

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<OrderItemDetailDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDetailDTO> items) {
        this.items = items;
    }
}
