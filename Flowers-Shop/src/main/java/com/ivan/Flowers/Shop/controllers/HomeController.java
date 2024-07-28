package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.services.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public ModelAndView home() {

        ModelAndView modelAndView = new ModelAndView("home");

        modelAndView.addObject("bouquets", homeService.getBouquets());

        return modelAndView;
    }

}
