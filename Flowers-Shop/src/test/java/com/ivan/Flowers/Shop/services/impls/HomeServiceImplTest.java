package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HomeServiceImplTest {

    public static final String URL = "/images/picture.jpg";
    public static final String DESCRIPTION = "Bouquet of flowers";

    @Mock
    private BouquetRepository mockedBouquetRepository;

    @Mock
    private Bouquet bouquet;

    @InjectMocks
    private HomeServiceImpl toTest;

    @BeforeEach
    void setUp() {

        bouquet = new Bouquet();
        bouquet.setId(1);
        bouquet.setUrl(URL);
        bouquet.setItemNumber(111);
        bouquet.setDescription(DESCRIPTION);
        bouquet.setPrice(25.25);
        bouquet.setQuantity(1);

    }

    @Test
    void testGetBouquets_return_Bouquets() {

        when(mockedBouquetRepository.findAll())
                .thenReturn(List.of(bouquet));

        List<BouquetDTO> bouquets = toTest.getBouquets();

        BouquetDTO bouquetDTO = bouquets.getFirst();

        Assertions.assertEquals(1, bouquets.size());
        Assertions.assertEquals(URL, bouquetDTO.getUrl());
        Assertions.assertEquals(25.25, bouquetDTO.getUnitPrice());
        Assertions.assertEquals(111, bouquetDTO.getItemNumber());
        Assertions.assertEquals(DESCRIPTION, bouquetDTO.getDescription());

    }
}
