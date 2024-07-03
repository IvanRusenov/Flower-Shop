package com.ivan.Flowers.Shop.models.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "bouquets")
public class Bouquet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, unique = true)
    private int itemNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bouquet bouquet = (Bouquet) o;
        return id == bouquet.id && Double.compare(price, bouquet.price) == 0 && quantity == bouquet.quantity && itemNumber == bouquet.itemNumber && Objects.equals(description, bouquet.description) && Objects.equals(url, bouquet.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, price, quantity, url, itemNumber);
    }
}
