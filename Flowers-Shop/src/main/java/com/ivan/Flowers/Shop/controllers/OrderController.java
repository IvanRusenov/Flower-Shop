package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.models.dtos.CreateOrderDTO;
import com.ivan.Flowers.Shop.models.dtos.OrderDTO;
import com.ivan.Flowers.Shop.services.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ModelAndView orders(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView myOrders = new ModelAndView("my-orders");

        myOrders.addObject("orders", orderService.getAllOrdersByUser(userDetails));

        return myOrders;
    }

    @PostMapping("/order/{cartId}")
    public ModelAndView order(@PathVariable("cartId") long cartId) {

        CreateOrderDTO createOrderDTO = orderService.createOrderDTO(cartId);
        orderService.createOrder(createOrderDTO);

        return new ModelAndView("redirect:/orders");
    }
}
