package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.models.dtos.OrderDTO;
import com.ivan.Flowers.Shop.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ModelAndView orders() {
        return new ModelAndView("orders");
    }

    @PostMapping("/order/{cartId}")
    public ModelAndView order(@PathVariable("cartId") long cartId) {

        OrderDTO orderDTO = orderService.createOrderDTO(cartId);
        orderService.createOrder(orderDTO);

        return new ModelAndView("redirect:/orders");
    }
}
