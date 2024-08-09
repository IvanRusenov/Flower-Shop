package com.ivan.Flowers.Shop.services.impls;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.ivan.Flowers.Shop.configs.OrderApiConfig;
import com.ivan.Flowers.Shop.enums.StatusType;
import com.ivan.Flowers.Shop.models.dtos.OrderDetailsDTO;
import com.ivan.Flowers.Shop.models.dtos.OrderItemDetailDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.models.entities.Cart;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.repositories.CartRepository;
import com.ivan.Flowers.Shop.services.OrderService;
import com.ivan.Flowers.Shop.services.exceptions.ObjectNotFoundException;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
@SpringBootTest
@EnableWireMock(@ConfigureWireMock(name = "orders-api-service"))
public class OrderServiceImplIT {

    @InjectWireMock(value = "orders-api-service")
    private WireMockServer wireMockServer;

    @Autowired
    private OrderApiConfig orderApiConfig;

    @Autowired
    private OrderService orderService;

    @Mock
    private CartRepository cartRepository;

    @Autowired
    private BouquetRepository bouquetRepository;

    @BeforeEach
    void setUp() {
        orderApiConfig.setBaseUrl(wireMockServer.baseUrl());
    }

    @Test
    public void testGetAllOrdersDesc() {

        wireMockServer.stubFor(get("/orders")
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                """
                                [
                                    {
                                        "id": 1,
                                        "userId": 2,
                                        "orderDateTime": "2024-08-08T12:30:27.287233",
                                        "totalAmount": 1322.0,
                                        "status": "PENDING",
                                        "shippingAddress": "c. Plovdiv ra. Trakia bl. 1234 fl. ",
                                        "items": [
                                            {
                                                "id": 1,
                                                "bouquetId": 1,
                                                "quantity": 1,
                                                "unitPrice": 1222.0
                                            }
                                        ]
                                    },
                                    {
                                        "id": 2,
                                        "userId": 3,
                                        "orderDateTime": "2024-08-08T13:45:10.123456",
                                        "totalAmount": 1580.0,
                                        "status": "SHIPPED",
                                        "shippingAddress": "c. Sofia ra. Mladost bl. 5678 fl. ",
                                        "items": [
                                            {
                                                "id": 2,
                                                "bouquetId": 2,
                                                "quantity": 2,
                                                "unitPrice": 790.0
                                            }
                                        ]
                                    }
                                ]
                                """
                        )
                        .withStatus(200))
        );

        Cart cart = new Cart();
        cart.setId(1);

        Bouquet bouquet1 = new Bouquet();
        bouquet1.setId(1);
        bouquet1.setUrl("/image/picture1.png");
        bouquet1.setItemNumber(10);
        bouquet1.setPrice(1222.0);
        bouquet1.setDescription("Beautiful bouquet");
        bouquet1.setQuantity(1);

        Bouquet bouquet2 = new Bouquet();
        bouquet2.setId(2);
        bouquet2.setUrl("/image/picture2.png");
        bouquet2.setItemNumber(20);
        bouquet2.setPrice(790.0);
        bouquet2.setDescription("Elegant bouquet");
        bouquet2.setQuantity(2);

        bouquetRepository.saveAll(List.of(bouquet1, bouquet2));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        OrderItemDetailDTO orderItem1 = new OrderItemDetailDTO();
        orderItem1.setId(1);
        orderItem1.setQuantity(1);
        orderItem1.setUrl(bouquet1.getUrl());
        orderItem1.setItemNumber(bouquet1.getItemNumber());
        orderItem1.setDescription(bouquet1.getDescription());
        orderItem1.setUnitPrice(bouquet1.getPrice());

        OrderItemDetailDTO orderItem2 = new OrderItemDetailDTO();
        orderItem2.setId(2);
        orderItem2.setQuantity(2);
        orderItem2.setUrl(bouquet2.getUrl());
        orderItem2.setItemNumber(bouquet2.getItemNumber());
        orderItem2.setDescription(bouquet2.getDescription());
        orderItem2.setUnitPrice(bouquet2.getPrice());

        OrderDetailsDTO expectedOrder1 = new OrderDetailsDTO();
        expectedOrder1.setId(1);
        expectedOrder1.setItems(List.of(orderItem1));
        expectedOrder1.setShippingAddress("c. Plovdiv ra. Trakia bl. 1234 fl. ");
        expectedOrder1.setStatus(StatusType.PENDING);
        expectedOrder1.setOrderDateTime(LocalDateTime.parse("2024-08-08T12:30:27.287233"));
        expectedOrder1.setTotalAmount(1322.0);

        OrderDetailsDTO expectedOrder2 = new OrderDetailsDTO();
        expectedOrder2.setId(2);
        expectedOrder2.setItems(List.of(orderItem2));
        expectedOrder2.setShippingAddress("c. Sofia ra. Mladost bl. 5678 fl. ");
        expectedOrder2.setStatus(StatusType.SHIPPED);
        expectedOrder2.setOrderDateTime(LocalDateTime.parse("2024-08-08T13:45:10.123456"));
        expectedOrder2.setTotalAmount(1580.0);

        List<OrderDetailsDTO> actualOrders = orderService.getAllOrdersDesc();

        assertEquals(2, actualOrders.size());

        OrderDetailsDTO actualOrder1 = actualOrders.get(0);
        assertEquals(expectedOrder1.getShippingAddress(), actualOrder1.getShippingAddress());
        assertEquals(expectedOrder1.getStatus(), actualOrder1.getStatus());
        assertEquals(expectedOrder1.getTotalAmount(), actualOrder1.getTotalAmount());
        assertEquals(expectedOrder1.getOrderDateTime(), actualOrder1.getOrderDateTime());

        OrderItemDetailDTO actualItem1 = actualOrder1.getItems().get(0);
        assertEquals(orderItem1.getDescription(), actualItem1.getDescription());
        assertEquals(orderItem1.getItemNumber(), actualItem1.getItemNumber());
        assertEquals(orderItem1.getQuantity(), actualItem1.getQuantity());
        assertEquals(orderItem1.getUnitPrice(), actualItem1.getUnitPrice());
        assertEquals(orderItem1.getUrl(), actualItem1.getUrl());

        OrderDetailsDTO actualOrder2 = actualOrders.get(1);
        assertEquals(expectedOrder2.getShippingAddress(), actualOrder2.getShippingAddress());
        assertEquals(expectedOrder2.getStatus(), actualOrder2.getStatus());
        assertEquals(expectedOrder2.getTotalAmount(), actualOrder2.getTotalAmount());
        assertEquals(expectedOrder2.getOrderDateTime(), actualOrder2.getOrderDateTime());

        OrderItemDetailDTO actualItem2 = actualOrder2.getItems().get(0);
        assertEquals(orderItem2.getDescription(), actualItem2.getDescription());
        assertEquals(orderItem2.getItemNumber(), actualItem2.getItemNumber());
        assertEquals(orderItem2.getQuantity(), actualItem2.getQuantity());
        assertEquals(orderItem2.getUnitPrice(), actualItem2.getUnitPrice());
        assertEquals(orderItem2.getUrl(), actualItem2.getUrl());
    }

    @Test
    public void testCreateOrder_CartNotFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> orderService.createOrder(1L));
    }

}
