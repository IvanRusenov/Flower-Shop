package com.ivan.flower_shop.orders.models.dtos;

public class OrderItemDTO {

    private long bouquetId;
    private int quantity;
    private double unitPrice;

    public long getBouquetId() {
        return bouquetId;
    }

    public void setBouquetId(long bouquetId) {
        this.bouquetId = bouquetId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
