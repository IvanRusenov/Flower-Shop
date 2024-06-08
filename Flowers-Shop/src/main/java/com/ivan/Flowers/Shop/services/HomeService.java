package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;

import java.util.List;

public interface HomeService {
    List<BouquetDTO> getBouquets();
}
