package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.services.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/cart")
    public ModelAndView cart(@AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView cart = new ModelAndView("cart");
        cart.addObject("bouquets", cartService.getBouquets(userDetails));
        cart.addObject("totalSum", cartService.getTotalSum(userDetails));

        return cart;
    }


    @GetMapping("cart/add/{itemNumber}")
    public ModelAndView addToCart(@PathVariable("itemNumber") int itemNumber,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        cartService.addToCart(itemNumber, userDetails);

        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/cart/remove/{itemNumber}")
    public ModelAndView remove(@PathVariable("itemNumber") int itemNumber,
                               @AuthenticationPrincipal UserDetails userDetails) {

        cartService.remove(itemNumber, userDetails);

        return new ModelAndView("redirect:/cart");
    }

}
