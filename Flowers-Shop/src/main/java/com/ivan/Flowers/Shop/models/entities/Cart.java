package com.ivan.Flowers.Shop.models.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<CartItem> items;

    private double totalPrice;

    @OneToOne(mappedBy = "cart")
    private User owner;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        setTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice = this.items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addCartItem(CartItem cartItem) {
        this.items.add(cartItem);
        setTotalPrice();
    }

    public void removeCartItem(CartItem cartItem) {
        this.items.remove(cartItem);
        setTotalPrice();
    }
}
