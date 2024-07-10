package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.CartItem;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CartService {
    void addToCart(int itemNumber, UserDetails userDetails);

    List<CartItem> getItems(UserDetails userDetails);

    double getTotalSum(UserDetails userDetails);

    void remove(long id, UserDetails userDetails);

    Cart getCart(UserDetails userDetails);
}
