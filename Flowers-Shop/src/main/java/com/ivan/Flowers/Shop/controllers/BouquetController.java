package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.services.BouquetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BouquetController {

    private final BouquetService bouquetService;

    public BouquetController(BouquetService bouquetService) {
        this.bouquetService = bouquetService;
    }


    @GetMapping("/bouquet/add")
    public ModelAndView addBouquet(ModelAndView modelAndView) {

        if (!modelAndView.getModel().containsKey("bouquetDTO")) {
            modelAndView.addObject("bouquetDTO", BouquetDTO.empty());
        }
        modelAndView.setViewName("bouquet-add");

        return modelAndView;

    }

    @PostMapping("/bouquet/add")
    public ModelAndView addBouquet(@Valid BouquetDTO bouquetDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("bouquet-add");
        }

        boolean isAdded = bouquetService.addBouquet(bouquetDTO);

        if (!isAdded) {
            return new ModelAndView("bouquet-add");
            //todo: return appropriate error message
        }

        return new ModelAndView("redirect:/home");
    }


}
