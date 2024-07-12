package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.enums.StatusType;
import com.ivan.Flowers.Shop.models.dtos.ChangeOrderStatusDTO;
import com.ivan.Flowers.Shop.models.dtos.CreateOrderDTO;
import com.ivan.Flowers.Shop.models.dtos.OrderDTO;
import com.ivan.Flowers.Shop.models.dtos.OrderDetailsDTO;
import com.ivan.Flowers.Shop.services.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ModelAndView orders(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView myOrders = new ModelAndView("my-orders");

        List<OrderDetailsDTO> allOrdersByUser = orderService.getAllOrdersFromUser(userDetails);
        myOrders.addObject("orders", allOrdersByUser);

        return myOrders;
    }

    @PostMapping("/order/{cartId}")
    public ModelAndView order(@PathVariable("cartId") long cartId) {

        orderService.createOrder(cartId);

        return new ModelAndView("redirect:/order/created");
    }



    @GetMapping("order/created")
    public ModelAndView created(@AuthenticationPrincipal UserDetails userDetails) {

        ModelAndView modelAndView = new ModelAndView("created-order");
        OrderDetailsDTO lastOrderByUser = orderService.getLastOrderByUser(userDetails);
        modelAndView.addObject("order", lastOrderByUser);
        modelAndView.addObject("username", StringUtils.capitalize(userDetails.getUsername()));

        return modelAndView;

    }

    @GetMapping("/order/change-status")
    private ModelAndView changeStatus(ChangeOrderStatusDTO changeOrderStatusDTO) {
        ModelAndView modelAndView = new ModelAndView("change-status");

        List<StatusType> statusTypes = Arrays.stream(StatusType.values()).toList();
        modelAndView.addObject("statusTypes", statusTypes);

        return modelAndView;
    }


    @PostMapping("/order/change-status/{id}")
    private ModelAndView changeStatus(@PathVariable("id") long id) {

        orderService.changOrderStatus(id, StatusType.SHIPPED);

        return new ModelAndView("redirect:/orders/pending");
    }


    @GetMapping("/orders/pending")
    public ModelAndView getPendingOrders() {
        ModelAndView shippingOrder = new ModelAndView("shipping-order");

        List<OrderDetailsDTO> allOrdersByUser = orderService.getAllPendingOrders();
        shippingOrder.addObject("orders", allOrdersByUser);

        return shippingOrder;
    }


}
