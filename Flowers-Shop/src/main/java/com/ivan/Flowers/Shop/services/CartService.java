package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.entities.Bouquet;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CartService {
    void addToCart(int itemNumber, UserDetails userDetails);

    List<Bouquet> getBouquets(UserDetails userDetails);

    double getTotalSum(UserDetails userDetails);

    void remove(int itemNumber, UserDetails userDetails);
}
