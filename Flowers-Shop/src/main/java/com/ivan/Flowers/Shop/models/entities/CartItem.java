package com.ivan.Flowers.Shop.models.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Bouquet bouquet;

    private int quantity;
    private double unitPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return id == cartItem.id && quantity == cartItem.quantity && Double.compare(unitPrice, cartItem.unitPrice) == 0 && Objects.equals(bouquet, cartItem.bouquet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bouquet, quantity, unitPrice);
    }
}
