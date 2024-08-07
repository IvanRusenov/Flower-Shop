package com.ivan.Flowers.Shop.controllers;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.services.BouquetService;
import com.ivan.Flowers.Shop.services.CloudinaryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BouquetControllerIT {

    private static final String DESCRIPTION = "Beautiful bouquet";
    private static final int ITEM_NUMBER = 1;
    private static final double UNIT_PRICE = 50.0;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BouquetRepository bouquetRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @MockBean
    private BouquetService bouquetService;

    @Mock
    private MultipartFile file;

    @BeforeEach
    @AfterEach
    void tearDown() {
        bouquetRepository.deleteAll();
    }

    private Bouquet createBouquet() {
        Bouquet bouquet = new Bouquet();
        bouquet.setDescription(DESCRIPTION);
        bouquet.setItemNumber(ITEM_NUMBER);
        bouquet.setPrice(UNIT_PRICE);
        bouquet.setQuantity(1);
        bouquet.setUrl("http://example.com/image.jpg");

        return bouquetRepository.save(bouquet);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddBouquetGet() throws Exception {
        mockMvc.perform(get("/bouquet/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bouquet-add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddBouquetPost_withValidData() throws Exception {

        when(cloudinaryService.uploadImage(any())).thenReturn("http://example.com/image.jpg");
        when(bouquetService.addBouquet(any())).thenReturn(true);

        when(file.getContentType()).thenReturn("image/jpg");

        BouquetDTO bouquetDTO = new BouquetDTO();
        bouquetDTO.setUnitPrice(20);
        bouquetDTO.setPicture(file);
        bouquetDTO.setDescription("khgf");
        bouquetDTO.setUrl("kugf");
        bouquetDTO.setItemNumber(10);

        mockMvc.perform(post("/bouquet/add")
                        .param("bouquetDTO", "bouquetDTO")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        when(bouquetService.addBouquet(any())).thenReturn(false);

        mockMvc.perform(post("/bouquet/add")
                        .param("bouquetDTO", "bouquetDTO")
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(view().name("bouquet-add"));

    }

}
