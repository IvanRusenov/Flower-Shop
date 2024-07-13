package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.enums.StatusType;
import com.ivan.Flowers.Shop.models.dtos.*;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.repositories.UserRepository;
import com.ivan.Flowers.Shop.services.CartService;
import com.ivan.Flowers.Shop.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final RestClient orderRestClient;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BouquetRepository bouquetRepository;
    private final CartService cartService;

    public OrderServiceImpl(@Qualifier("ordersRestClient") RestClient orderRestClient, CartRepository cartRepository, ModelMapper modelMapper, UserRepository userRepository, BouquetRepository bouquetRepository, CartService cartService) {
        this.orderRestClient = orderRestClient;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
        this.cartService = cartService;
    }

    @Override
    public void createOrder(long cartId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow();

        CreateOrderDTO createOrderDTO = modelMapper.map(cart, CreateOrderDTO.class);
        createOrderDTO.setUserId(cart.getOwner().getId());
        createOrderDTO.setShippingAddress(cart.getOwner().getShippingAddress());

        LOGGER.info("Creating new order");
        orderRestClient
                .post()
                .uri("/orders")//"http://localhost:8888/orders"
                .body(createOrderDTO)
                .retrieve();

        cartService.clearCart(cartId);

    }

    @Override
    public List<OrderDetailsDTO> getAllOrdersFromUser(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        LOGGER.info("Get all user orders in descending order");

        User user = userRepository.findByUsername(shopUserDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderDTO> orderDTOS = orderRestClient
                .get()
                .uri("/orders/user/" + user.getId())
                .accept(MediaType.asMediaType(MediaType.APPLICATION_JSON))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });


        if (orderDTOS == null) {
            throw new RuntimeException("Failed to retrieve orders");
        }

        return orderDTOS.stream().map(orderDTO -> {

            OrderDetailsDTO orderDetailsDTO = modelMapper.map(orderDTO, OrderDetailsDTO.class);

            List<OrderItemDetailDTO> orderItemDetailDTOS = orderDTO.getItems().stream().map(orderItemDTO -> {

                OrderItemDetailDTO orderItemDetailDTO = modelMapper.map(orderItemDTO, OrderItemDetailDTO.class);
                Bouquet bouquet = bouquetRepository.findById(orderItemDTO.getBouquetId())
                        .orElseThrow(
                                () -> new NoSuchElementException("Bouquet not found for ID "
                                        + orderItemDTO.getBouquetId())
                        );

                orderItemDetailDTO.setItemNumber(bouquet.getItemNumber());
                orderItemDetailDTO.setDescription(bouquet.getDescription());
                orderItemDetailDTO.setUrl(bouquet.getUrl());

                return orderItemDetailDTO;

            }).toList();

            orderDetailsDTO.setItems(orderItemDetailDTOS);
            return orderDetailsDTO;

        }).toList();

    }

    @Override
    public OrderDetailsDTO getLastOrderByUser(UserDetails userDetails) {

        LOGGER.info("Get last user order");
        return getAllOrdersFromUser(userDetails).getFirst();

    }

    @Override
    public void changOrderStatus(long orderId, StatusType newStatus) {

        orderRestClient
                .put()
                .uri("/orders/" + orderId + "/" + newStatus)
//                .accept(MediaType.asMediaType(MediaType.APPLICATION_JSON))
                .retrieve();

    }

    @Override
    public List<OrderDetailsDTO> getAllPendingOrders() {

        List<OrderDTO> orderDTOS =  orderRestClient
                .get()
                .uri("/orders/pending")
                .accept(MediaType.asMediaType(MediaType.APPLICATION_JSON))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (orderDTOS == null) {
            throw new RuntimeException("Failed to retrieve orders");
        }


        return orderDTOS.stream().map(orderDTO -> {

            OrderDetailsDTO orderDetailsDTO = modelMapper.map(orderDTO, OrderDetailsDTO.class);

            List<OrderItemDetailDTO> orderItemDetailDTOS = orderDTO.getItems().stream().map(orderItemDTO -> {

                OrderItemDetailDTO orderItemDetailDTO = modelMapper.map(orderItemDTO, OrderItemDetailDTO.class);
                Bouquet bouquet = bouquetRepository.findById(orderItemDTO.getBouquetId())
                        .orElseThrow(
                                () -> new NoSuchElementException("Bouquet not found for ID "
                                        + orderItemDTO.getBouquetId())
                        );

                orderItemDetailDTO.setItemNumber(bouquet.getItemNumber());
                orderItemDetailDTO.setDescription(bouquet.getDescription());
                orderItemDetailDTO.setUrl(bouquet.getUrl());

                return orderItemDetailDTO;

            }).toList();

            orderDetailsDTO.setItems(orderItemDetailDTOS);
            return orderDetailsDTO;

        }).toList();

    }

    @Override
    public List<OrderDetailsDTO> getAllOrdersDesc() {

        List<OrderDTO> orderDTOS =  orderRestClient
                .get()
                .uri("/orders")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (orderDTOS == null) {
            throw new RuntimeException("Failed to retrieve orders");
        }


        return orderDTOS.stream().map(orderDTO -> {

            OrderDetailsDTO orderDetailsDTO = modelMapper.map(orderDTO, OrderDetailsDTO.class);

            List<OrderItemDetailDTO> orderItemDetailDTOS = orderDTO.getItems().stream().map(orderItemDTO -> {

                OrderItemDetailDTO orderItemDetailDTO = modelMapper.map(orderItemDTO, OrderItemDetailDTO.class);
                Bouquet bouquet = bouquetRepository.findById(orderItemDTO.getBouquetId())
                        .orElseThrow(
                                () -> new NoSuchElementException("Bouquet not found for ID "
                                        + orderItemDTO.getBouquetId())
                        );

                orderItemDetailDTO.setItemNumber(bouquet.getItemNumber());
                orderItemDetailDTO.setDescription(bouquet.getDescription());
                orderItemDetailDTO.setUrl(bouquet.getUrl());

                return orderItemDetailDTO;

            }).toList();

            orderDetailsDTO.setItems(orderItemDetailDTOS);
            return orderDetailsDTO;

        }).toList();
    }

//    private void validateUser(UserDetails userDetails) {
//        if (!(userDetails instanceof ShopUserDetails)) {
//            throw new RuntimeException("User is not authenticated.");
//        }
//    }


}
