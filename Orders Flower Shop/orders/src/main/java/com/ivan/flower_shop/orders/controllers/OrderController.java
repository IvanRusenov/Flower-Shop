package com.ivan.flower_shop.orders.controllers;

import com.ivan.flower_shop.orders.models.dtos.OrderDTO;
import com.ivan.flower_shop.orders.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {

        LOGGER.info("Going to create order {}", orderDTO);

        orderService.createOrder(orderDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public  List<OrderDTO> getAllOrders() {

        LOGGER.info("Showing all orders");

        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable Long orderId) {

        LOGGER.info("Get order with id {}", orderId);

        return orderService.getOrderById(orderId);
    }

    /**
     * <strong>{@code Deletes}</strong> an order with id {@code @param orderId} from database.
     * <p><em>NOTE:</em> It also deletes {@code orderItems} witch belongs to this order!
     * <p><strong>{@code Tested!}</strong></p>
     *
     * @author <p><strong>{@code Ivan Rusenov}</strong></p>
     */
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {

        orderService.deleteOrder(orderId);
        LOGGER.info("Order with {} was deleted!", orderId);
    }

    @PutMapping("/{orderId}")
    public void updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {

        orderService.updateOrder(orderId, orderDTO);
        LOGGER.info("Order with {} was updated", orderId);

    }

}
