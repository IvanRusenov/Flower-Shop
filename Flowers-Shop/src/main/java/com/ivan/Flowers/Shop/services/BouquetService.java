package com.ivan.Flowers.Shop.services;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BouquetService {

    boolean addBouquet(BouquetDTO bouquetDTO) throws IOException;
}
