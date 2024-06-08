package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
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
    public List<BouquetDTO> getBouquets() {

       return bouquetRepository
               .findAll()
               .stream()
               .map(b-> new BouquetDTO(b.getDescription(), b.getItemNumber(), b.getPrice(), b.getUrl()))
               .toList();

    }
}
