package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.services.BouquetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Optional;

@Service
public class BouquetServiceImpl implements BouquetService {
    //    public static final String IMAGES_PATH = "src/main/resources/static/img/";
//    private static final String URL_PATH = "/img/";
    private static final String URL_PATH = "img/";
    private static final String IMAGES_PATH = ".\\src\\main\\resources\\static\\img\\";
    private final BouquetRepository bouquetRepository;

    public BouquetServiceImpl(BouquetRepository bouquetRepository) {
        this.bouquetRepository = bouquetRepository;
    }

    @Override
    @Transactional
    public boolean addBouquet(BouquetDTO bouquetDTO) throws IOException {//@RequestParam("file") MultipartFile pictureFile
//        todo: BUG WITH ADDED BOUQUET PICTURES PICTURES: SHOWS WHEN RESTART APP - async operations

        Optional<Bouquet> optionalBouquet = bouquetRepository.findByItemNumber(bouquetDTO.getItemNumber());

        if (optionalBouquet.isPresent()) {
            return false;
        }

        uploadFile(bouquetDTO);




        Bouquet bouquet = new Bouquet();
        String url = URL_PATH + bouquetDTO.getPicture().getOriginalFilename();
        bouquet.setUrl(url);
        bouquet.setDescription(bouquetDTO.getDescription());
        bouquet.setItemNumber(bouquetDTO.getItemNumber());
        bouquet.setPrice(bouquetDTO.getPrice());
        bouquet.setQuantity(bouquet.getQuantity() + 1);

        bouquetRepository.saveAndFlush(bouquet);


        return true;


    }

    private static void uploadFile(BouquetDTO bouquetDTO) throws IOException {
        MultipartFile picture = bouquetDTO.getPicture();


        File file = new File(IMAGES_PATH + picture.getOriginalFilename());
        boolean newFile = file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(picture.getBytes());
    }

}
