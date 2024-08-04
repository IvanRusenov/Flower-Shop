package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.services.HomeService;
import com.ivan.Flowers.Shop.services.impls.CartServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final HomeService homeService;
    private final CartServiceImpl cartService;

    public HomeController(HomeService homeService, CartServiceImpl cartService) {
        this.homeService = homeService;
        this.cartService = cartService;
    }

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView modelAndView = new ModelAndView("home");

        modelAndView.addObject("cartItemsCount", cartService.getAllCartItemsQuantity(userDetails));
        modelAndView.addObject("bouquets", homeService.getBouquets());

        return modelAndView;
    }

}
