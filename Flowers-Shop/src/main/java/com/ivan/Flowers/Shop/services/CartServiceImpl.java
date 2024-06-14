package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import com.ivan.Flowers.Shop.session.LoggedUser;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private  final BouquetRepository bouquetRepository;

    private final CartRepository cartRepository;
    private final LoggedUser loggedUser;

    public CartServiceImpl(UserRepository userRepository, BouquetRepository bouquetRepository, CartRepository cartRepository, LoggedUser loggedUser) {
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
        this.cartRepository = cartRepository;
        this.loggedUser = loggedUser;
    }

    @Override
    @Transactional
    public void addToCart(int itemNumber) {

        Bouquet bouquet = bouquetRepository.findByItemNumber(itemNumber).get();

        Optional<User> optionalUser = userRepository.findByUsername(loggedUser.getUsername());
        Cart cart = optionalUser.get().getCart();

        cart.getBouquets().add(bouquet);


        cart.setTotalPrice(cart.getBouquets().stream().mapToDouble(Bouquet::getPrice).sum());

        userRepository.saveAndFlush(optionalUser.get());
        cartRepository.saveAndFlush(cart);

    }
}
