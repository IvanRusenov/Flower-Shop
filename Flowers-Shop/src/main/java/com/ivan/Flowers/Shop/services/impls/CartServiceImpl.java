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
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

        User user = userRepository.findByUsername(shopUserDetails.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("User not found", User.class.getSimpleName()));
        Bouquet bouquet = bouquetRepository.findByItemNumber(itemNumber)
                .orElseThrow(() -> new ObjectNotFoundException("Bouquet not found", Bouquet.class.getSimpleName()));
        Cart cart = user.getCart();

        CartItem cartItem = cart.getItems().stream()
                .filter(ci -> ci.getBouquet().getId() == bouquet.getId())
                .findFirst()
                .orElse(null);

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
    public void clearCart(long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ObjectNotFoundException("Cart doesn't exist", Cart.class.getSimpleName()));

        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);

    }

    @Override
    @Transactional
    public List<CartItem> getItems(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("User not found", User.class.getSimpleName()));
        return user.getCart().getItems();

    }

    @Override
    @Transactional
    public double getTotalSum(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("User not found", User.class.getSimpleName()));

        return user.getCart().getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
//        return String.format("%.2f", sum);

    }

    @Override
    public void remove(long id, UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        String username = shopUserDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found", User.class.getSimpleName()));

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cart item not found", CartItem.class.getSimpleName()));


        Cart cart = cartRepository.findById(user.getCart().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Cart not found", Cart.class.getSimpleName()));


        cart.getItems().remove(cartItem);
        cart.setTotalPrice(cart.getItems().stream().mapToDouble(CartItem::getUnitPrice).sum());

        cartRepository.saveAndFlush(cart);
        cartItemRepository.delete(cartItem);
        userRepository.saveAndFlush(user);

    }

}
