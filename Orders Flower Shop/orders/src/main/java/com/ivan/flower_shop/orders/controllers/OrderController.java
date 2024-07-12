package com.ivan.flower_shop.orders.controllers;

import com.ivan.flower_shop.orders.enums.StatusType;
import com.ivan.flower_shop.orders.models.dtos.OrderDTO;
import com.ivan.flower_shop.orders.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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


        orderService.createOrder(orderDTO);
        LOGGER.info("New order was created");

//        return ResponseEntity.status(201).build();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {

        LOGGER.info("Showing all orders");

        List<OrderDTO> allOrders = orderService.getAllOrders();

        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersFromUser(@PathVariable Long userId) {

        LOGGER.info("Get order with id {}", userId);

        List<OrderDTO> allOrdersByUser = orderService.getAllOrdersFromUser(userId);

        if (allOrdersByUser == null) {

            LOGGER.info("Cannot find order with id {}", userId);

            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok(allOrdersByUser);
    }



    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {

        LOGGER.info("Get order with id {}", orderId);

        OrderDTO orderById = orderService.getOrderById(orderId);

        if (orderById == null) {

            LOGGER.info("Cannot find order with id {}", orderId);

            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok(orderById);
    }

    /**
     * <strong>{@code Deletes}</strong> an order with id {@code @param orderId} from database.
     * <p><em>NOTE:</em> It also deletes {@code orderItems} witch belongs to this order!
     * <p><strong>{@code Tested!}</strong></p>
     *
     * @author <p><strong>{@code Ivan Rusenov}</strong></p>
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {

        orderService.deleteOrder(orderId);
        LOGGER.info("Order with {} was deleted!", orderId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/{newStatus}")
    public ResponseEntity<Void> updateOrder(@PathVariable Long orderId, @PathVariable StatusType newStatus) {

        orderService.updateOrder(orderId, newStatus);
        LOGGER.info("Order with {} was updated", orderId);

        return ResponseEntity.ok().build();
    }



    @GetMapping("/pending")
    public ResponseEntity<List<OrderDTO>> getAllOPendingOrders() {

        LOGGER.info("Get all pending orders");

        List<OrderDTO> allPendingOrders = orderService.getAllPendingOrders();

        if (allPendingOrders == null) {

            LOGGER.info("Cannot find pending orders");

            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok(allPendingOrders);
    }

}
