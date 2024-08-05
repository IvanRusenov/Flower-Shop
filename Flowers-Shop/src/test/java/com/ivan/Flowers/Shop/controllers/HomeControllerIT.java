package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.services.HomeService;
import com.ivan.Flowers.Shop.services.impls.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomeService homeService;

    @MockBean
    private CartServiceImpl cartService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testHome() throws Exception {

        int cartItemsCount = 5;
        List<BouquetDTO> bouquets = Arrays.asList(new BouquetDTO(), new BouquetDTO());

        when(cartService.getAllCartItemsQuantity(any())).thenReturn(cartItemsCount);
        when(homeService.getBouquets()).thenReturn(bouquets);

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("cartItemsCount", cartItemsCount))
                .andExpect(model().attribute("bouquets", bouquets));

    }

}
