package com.ivan.Flowers.Shop.models.dtos;

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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
