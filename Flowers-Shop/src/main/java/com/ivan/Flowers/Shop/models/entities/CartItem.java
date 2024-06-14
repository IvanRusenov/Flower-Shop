package com.ivan.Flowers.Shop.models.entities;

public class CartItem {

    private Bouquet bouquet;
    private int quantity;


    public Bouquet getBouquet() {
        return bouquet;
    }

    public void setBouquet(Bouquet bouquet) {
        this.bouquet = bouquet;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
