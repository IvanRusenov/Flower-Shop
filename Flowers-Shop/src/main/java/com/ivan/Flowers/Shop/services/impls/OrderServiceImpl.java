package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.CreateOrderDTO;
import com.ivan.Flowers.Shop.models.dtos.OrderDTO;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.models.entities.User;
import com.ivan.Flowers.Shop.models.user.ShopUserDetails;
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

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final RestClient orderRestClient;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public OrderServiceImpl(@Qualifier("ordersRestClient") RestClient orderRestClient, CartRepository cartRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.orderRestClient = orderRestClient;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
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
    public List<OrderDTO> getAllOrdersByUser(UserDetails userDetails) {

        if (!(userDetails instanceof ShopUserDetails shopUserDetails)) {
            throw new RuntimeException("User is not authenticated.");
        }

        LOGGER.info("Get all user orders");

        User user = userRepository.findByUsername(shopUserDetails.getUsername()).orElseThrow();

         return orderRestClient
                .get()
                .uri("/orders/"+ user.getId())
                .accept(MediaType.asMediaType(MediaType.APPLICATION_JSON))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});


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
