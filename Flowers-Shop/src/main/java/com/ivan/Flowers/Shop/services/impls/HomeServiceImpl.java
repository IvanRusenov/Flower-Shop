package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.services.HomeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    private final BouquetRepository bouquetRepository;

    public HomeServiceImpl(BouquetRepository bouquetRepository) {
        this.bouquetRepository = bouquetRepository;
    }

    @Override
    public List<Bouquet> getBouquets() {
       return bouquetRepository.findAll();
    }
}
