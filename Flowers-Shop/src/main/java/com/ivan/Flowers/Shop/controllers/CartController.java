package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

    @GetMapping("cart/add/{itemNumber}")
    public ModelAndView addToCart(@PathVariable("itemNumber") int itemNumber) {

        cartService.addToCart(itemNumber);

        return new ModelAndView("redirect:/home");
    }


}
