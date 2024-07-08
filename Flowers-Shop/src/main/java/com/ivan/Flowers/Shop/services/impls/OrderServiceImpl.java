package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.*;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.repositories.UserRepository;
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

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final RestClient orderRestClient;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private  final BouquetRepository bouquetRepository;

    public OrderServiceImpl(@Qualifier("ordersRestClient") RestClient orderRestClient, CartRepository cartRepository, ModelMapper modelMapper, UserRepository userRepository, BouquetRepository bouquetRepository) {
        this.orderRestClient = orderRestClient;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
    }

    @Override
    public CreateOrderDTO createOrderDTO(long cartId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow();

        CreateOrderDTO createOrderDTO = modelMapper.map(cart, CreateOrderDTO.class);
        createOrderDTO.setUserId(cart.getOwner().getId());
        createOrderDTO.setShippingAddress(cart.getOwner().getShippingAddress());

        return createOrderDTO;
    }

    @Override
    public List<OrderDetailsDTO> getAllOrdersByUser(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        LOGGER.info("Get all user orders");

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();

        List<OrderDTO> orderDTOS = orderRestClient
                .get()
                .uri("/orders/" + user.getId())
                .accept(MediaType.asMediaType(MediaType.APPLICATION_JSON))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        List<OrderDetailsDTO> orderDetailsDTOS = orderDTOS.stream().map(orderDTO -> {

            OrderDetailsDTO orderDetailsDTO = modelMapper.map(orderDTO, OrderDetailsDTO.class);

            List<OrderItemDetailDTO> orderItemDetailDTOS = orderDTO.getItems().stream().map(orderItemDTO -> {

                OrderItemDetailDTO orderItemDetailDTO = modelMapper.map(orderItemDTO, OrderItemDetailDTO.class);
                Bouquet bouquet = bouquetRepository.findById(orderItemDTO.getBouquetId()).orElseThrow();
                orderItemDetailDTO.setItemNumber(bouquet.getItemNumber());
                orderItemDetailDTO.setDescription(bouquet.getDescription());
                orderItemDetailDTO.setUrl(bouquet.getUrl());

                return orderItemDetailDTO;

            }).toList();
            orderDetailsDTO.setItems(orderItemDetailDTOS);
            return orderDetailsDTO;

        }).toList();

        return orderDetailsDTOS;

    }






    @Override
    public void createOrder(CreateOrderDTO orderDTO) {

        LOGGER.info("Creating new order");
        orderRestClient
                .post()
                .uri("/orders")//"http://localhost:8888/orders"
//                .accept(MediaType.APPLICATION_JSON)
                .body(orderDTO)
                .retrieve();
    }
}
