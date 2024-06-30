package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.services.BouquetService;
import com.ivan.Flowers.Shop.services.CloudinaryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BouquetServiceImpl implements BouquetService {

    private final CloudinaryService cloudinaryService;
    private final BouquetRepository bouquetRepository;

    public BouquetServiceImpl(CloudinaryService cloudinaryService, BouquetRepository bouquetRepository) {
        this.cloudinaryService = cloudinaryService;
        this.bouquetRepository = bouquetRepository;
    }

    @Override
    public boolean addBouquet(BouquetDTO bouquetDTO) throws IOException {

        Optional<Bouquet> optionalBouquet = bouquetRepository.findByItemNumber(bouquetDTO.getItemNumber());

        if (optionalBouquet.isPresent()) {
            return false;
        }

        List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif", "image/jpg", "image/webp");
        String fileContentType = bouquetDTO.getPicture().getContentType();
        if (!contentTypes.contains(fileContentType)) {
            return false;
        }

        //todo: validate file size < 5mb

        Bouquet bouquet = new Bouquet();
        String url = cloudinaryService.uploadImage(bouquetDTO.getPicture());
        bouquet.setUrl(url);
        bouquet.setDescription(bouquetDTO.getDescription());
        bouquet.setItemNumber(bouquetDTO.getItemNumber());
        bouquet.setPrice(bouquetDTO.getPrice());
        bouquet.setQuantity(bouquet.getQuantity() + 1);

        bouquetRepository.saveAndFlush(bouquet);

        return true;
    }

}
