package com.ivan.Flowers.Shop.models.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
//    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private List<Bouquet> bouquets;

    private double totalPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Bouquet> getBouquets() {
        return bouquets;
    }

    public void setBouquets(List<Bouquet> bouquets) {
        this.bouquets = bouquets;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
