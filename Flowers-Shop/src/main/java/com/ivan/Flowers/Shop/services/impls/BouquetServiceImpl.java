package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.services.BouquetService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BouquetServiceImpl implements BouquetService {

    private final BouquetRepository bouquetRepository;

    public BouquetServiceImpl(BouquetRepository bouquetRepository) {
        this.bouquetRepository = bouquetRepository;
    }

    @Override
    public boolean addBouquet(BouquetDTO bouquetDTO) {

        Optional<Bouquet> optionalBouquet = bouquetRepository.findByItemNumber(bouquetDTO.itemNumber());

        if (optionalBouquet.isPresent()) {
            return false;
        }

        Bouquet bouquet = new Bouquet();
        bouquet.setDescription(bouquetDTO.description());
        bouquet.setItemNumber(bouquetDTO.itemNumber());
        bouquet.setPrice(bouquetDTO.price());
        bouquet.setUrl(bouquetDTO.url());
        bouquet.setQuantity(bouquet.getQuantity() + 1);

        bouquetRepository.saveAndFlush(bouquet);

        return true;
    }
}
