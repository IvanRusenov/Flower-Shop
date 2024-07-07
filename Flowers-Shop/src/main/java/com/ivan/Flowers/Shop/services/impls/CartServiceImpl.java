package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.CartItem;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.CartItemRepository;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import com.ivan.Flowers.Shop.services.CartService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final BouquetRepository bouquetRepository;
    private final CartItemRepository cartItemRepository;

    private final CartRepository cartRepository;

    public CartServiceImpl(UserRepository userRepository, BouquetRepository bouquetRepository, CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;

    }

    @Override
    @Transactional
    public void addToCart(int itemNumber, UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

//        TODO: use getBouquets and getTotalSum

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();
        Bouquet bouquet = bouquetRepository.findByItemNumber(itemNumber).orElseThrow();
        Cart cart = user.getCart();

        CartItem cartItem = cart.getItems().stream().filter(ci -> ci.getBouquet().getId() == bouquet.getId()).findFirst().orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setBouquet(bouquet);
            cartItem.setQuantity(1);
            cartItem.setUnitPrice(bouquet.getPrice());
            cart.getItems().add(cartItem);
            cartItemRepository.save(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        cart.setTotalPrice(cart.getItems().stream().mapToDouble(CartItem::getUnitPrice).sum());
        cartRepository.save(cart);

    }


    @Override
    public Cart getCart(UserDetails userDetails) {
        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();
        return user.getCart();
    }

    @Override
    @Transactional
    public List<CartItem> getItems(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();
        return user.getCart().getItems();

    }

    @Override
    @Transactional
    public String getTotalSum(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();

        double sum = user.getCart().getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        return String.format("%.2f", sum);
        //TODO: Remove BUG sum is not correct on adding more than one item of the same type

    }

    @Override
    public void remove(long id, UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        String username = shopUserDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));


        Cart cart = cartRepository.findById(user.getCart().getId())
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + username));


        cart.getItems().remove(cartItem);
        cart.setTotalPrice(cart.getItems().stream().mapToDouble(CartItem::getUnitPrice).sum());

        cartRepository.saveAndFlush(cart);
        cartItemRepository.delete(cartItem);
        userRepository.saveAndFlush(user);

    }



}
