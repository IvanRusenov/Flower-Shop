package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
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

    private final CartRepository cartRepository;

    public CartServiceImpl(UserRepository userRepository, BouquetRepository bouquetRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
        this.cartRepository = cartRepository;

    }

    @Override
    @Transactional
    public void addToCart(int itemNumber, UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            return;
            //todo: Display appropriate messages
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();
        Bouquet bouquet = bouquetRepository.findByItemNumber(itemNumber).orElseThrow();
        Cart cart = user.getCart();
//        int size = cart.getBouquets().size();
        cart.getBouquets().add(bouquet);
        cart.setTotalPrice(cart.getBouquets().stream().mapToDouble(Bouquet::getPrice).sum());

//        cartRepository.saveAndFlush(cart);
//        userRepository.saveAndFlush(user);

    }

    @Override
    @Transactional
    public List<Bouquet> getBouquets(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("anonymous");
            //todo: Display appropriate messages
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();
        return user.getCart().getBouquets();

    }

    @Override
    @Transactional
    public double getTotalSum(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("anonymous");
            //todo: Display appropriate messages
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();

        return user.getCart().getBouquets().
                stream()
                .mapToDouble(Bouquet::getPrice)
                .sum();

    }

    @Override
    public void remove(int itemNumber, UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("anonymous");
            //todo: Display appropriate messages
        }

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();
        Cart cart = cartRepository.findById(user.getCart().getId()).orElseThrow();

        Bouquet bouquet = bouquetRepository.findByItemNumber(itemNumber).orElseThrow();
//
//        Cart cart = user.getCart();
        cart.getBouquets().remove(bouquet);
        cartRepository.saveAndFlush(cart);
//        userRepository.saveAndFlush(user);
    }


}
