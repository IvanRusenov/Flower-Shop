package com.ivan.Flowers.Shop.models.entities;

import com.ivan.Flowers.Shop.enums.StatusType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Column(nullable = false)
    private double totalPrice;

    @OneToMany
    private List<Bouquet> bouquets;


    @ManyToOne
    private User orderBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Bouquet> getBouquets() {
        return bouquets;
    }

    public void setBouquets(List<Bouquet> bouquets) {
        this.bouquets = bouquets;
    }

    public User getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(User orderBy) {
        this.orderBy = orderBy;
    }

    //    private User orderBy;
}
