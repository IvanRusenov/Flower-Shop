package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.OrderDTO;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final RestClient orderRestClient;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(@Qualifier("ordersRestClient") RestClient orderRestClient, CartRepository cartRepository, ModelMapper modelMapper) {
        this.orderRestClient = orderRestClient;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDTO createOrderDTO(long cartId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow();

        OrderDTO orderDTO = modelMapper.map(cart, OrderDTO.class);
        orderDTO.setUserId(cart.getOwner().getId());
        orderDTO.setShippingAddress(cart.getOwner().getShippingAddress());

        return orderDTO;
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {

        LOGGER.info("Creating new offer");
        orderRestClient
                .post()
                .uri("/orders")//"http://localhost:8888/orders"
//                .accept(MediaType.APPLICATION_JSON)
                .body(orderDTO)
                .retrieve();
    }
}
