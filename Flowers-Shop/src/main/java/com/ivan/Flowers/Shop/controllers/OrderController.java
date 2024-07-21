package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.enums.StatusType;
import com.ivan.Flowers.Shop.models.dtos.ChangeOrderStatusDTO;
import com.ivan.Flowers.Shop.models.dtos.OrderDetailsDTO;
import com.ivan.Flowers.Shop.services.impls.OrderServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Controller
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
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
    public ModelAndView create(@PathVariable("cartId") long cartId) {

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

    @GetMapping("/orders/all")
    public ModelAndView getAllOrdersDesc() {

        ModelAndView orders = new ModelAndView("orders");

        orders.addObject("orders", orderService.getAllOrdersDesc());

        return orders;

    }

    @PostMapping("/orders/{id}")
    public ModelAndView delete(@PathVariable("id") long id) {

        orderService.delete(id);
        //todo: Show message {order with id was deleted}

        return new ModelAndView("redirect:/orders/all");

    }

    @GetMapping("/order/edit/{id}")
    public ModelAndView edit(@PathVariable("id") long id) {

        ModelAndView editOrder = new ModelAndView("edit-order");

        OrderDetailsDTO order = orderService.getOrder(id);
        editOrder.addObject("order", order);
        editOrder.addObject("statuses", StatusType.values());

        return editOrder;

    }

    @GetMapping("/order/edit")
    public ModelAndView edit() {

        ModelAndView editOrder = new ModelAndView("edit-order");

        editOrder.addObject("order", orderService.getCurrentOrder());
        editOrder.addObject("statuses", StatusType.values());

        return editOrder;

        //todo: Show message {order with id was edited}

    }

    @GetMapping("/order/deleteItem/{itemId}/{orderId}")
    public ModelAndView deleteItem(@PathVariable("itemId") long itemId, @PathVariable("orderId") long orderId) {

        ModelAndView editOrder = new ModelAndView("redirect:/order/edit");

        orderService.deleteItem(itemId, orderId);

        return editOrder;
    }

    @GetMapping("/order/cancel")
    public ModelAndView cansel() {

        orderService.cancelOrder();

        return new ModelAndView("redirect:/orders/all");

    }

    @PostMapping("/order/save")
    public ModelAndView saveOrder(OrderDetailsDTO orderDetailsDTO) {

        ModelAndView editOrder = new ModelAndView("redirect:/orders/all");

        orderService.edit(orderDetailsDTO);

        return editOrder;
    }
}
