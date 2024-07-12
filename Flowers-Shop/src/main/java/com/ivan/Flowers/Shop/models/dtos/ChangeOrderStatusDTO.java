package com.ivan.Flowers.Shop.models.dtos;

import com.ivan.Flowers.Shop.enums.StatusType;

public class ChangeOrderStatusDTO {

    private long orderId;
    private StatusType newStatus;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public StatusType getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(StatusType newStatus) {
        this.newStatus = newStatus;
    }
}
