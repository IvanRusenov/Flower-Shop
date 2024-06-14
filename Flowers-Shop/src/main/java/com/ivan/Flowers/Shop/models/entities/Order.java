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

    @OneToMany
    private List<Bouquet> bouquets;

    @ManyToOne
    private User orderBy;



//    private User orderBy;
}
