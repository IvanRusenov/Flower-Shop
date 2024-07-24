package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;

import java.io.IOException;

public interface BouquetService {
    boolean addBouquet(BouquetDTO bouquetDTO) throws IOException;

    void removeBouquet(int itemNumber) throws IOException;
}
