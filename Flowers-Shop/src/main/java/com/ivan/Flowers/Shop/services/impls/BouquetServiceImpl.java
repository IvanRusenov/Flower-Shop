package com.ivan.Flowers.Shop.services.impls;

import com.ivan.Flowers.Shop.models.dtos.BouquetDTO;
import com.ivan.Flowers.Shop.models.entities.Bouquet;
import com.ivan.Flowers.Shop.repositories.BouquetRepository;
import com.ivan.Flowers.Shop.services.BouquetService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BouquetServiceImpl implements BouquetService {
        public static final String IMAGES_PATH = "src/main/resources/static/img";
        private static final  String URL_PATH = "img/";
    //    public static final String IMAGES_PATH = ".\\src\\main\\resources\\static\\img\\";
    private final BouquetRepository bouquetRepository;

    public BouquetServiceImpl(BouquetRepository bouquetRepository) {
        this.bouquetRepository = bouquetRepository;
    }

    @Override
    public boolean addBouquet(BouquetDTO bouquetDTO) {

        Optional<Bouquet> optionalBouquet = bouquetRepository.findByItemNumber(bouquetDTO.getItemNumber());

        if (optionalBouquet.isPresent()) {
            return false;
        }

        //todo: file type validation
        List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
        String fileContentType = bouquetDTO.getPicture().getContentType();
        if(!contentTypes.contains(fileContentType)) {
            return false;
        }

//        todo: validate file size < 5mb

        //todo: save picture to the images
        Bouquet bouquet = new Bouquet();

        MultipartFile pictureFile = bouquetDTO.getPicture();
        File file = new File(IMAGES_PATH +"/"+pictureFile.getOriginalFilename());

        if (!file.exists() && file.isDirectory()) {
            try {
//            file.createNewFile();
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(pictureFile.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.getStackTrace();
            }

        }

        String url = URL_PATH + file.getName();
        bouquet.setUrl(url);
        bouquet.setDescription(bouquetDTO.getDescription());
        bouquet.setItemNumber(bouquetDTO.getItemNumber());
        bouquet.setPrice(bouquetDTO.getPrice());



        //todo: set picture URL
        bouquet.setQuantity(bouquet.getQuantity() + 1);

        bouquetRepository.saveAndFlush(bouquet);

        return true;
    }

//    private void uploadPicture(BouquetDTO bouquetDTO) {
//        String imagesPath = "src/main/resources/static/img";
//        String pictureName = String.valueOf(bouquetDTO.itemNumber());
//        MultipartFile picture = bouquetDTO.picture();
////        picture.getContentType();
////        String[] splitFilename = picture.getOriginalFilename().split("\\.");
////        String extension = splitFilename[splitFilename.length - 1];
//
//
//        try {
//            File file = new File(imagesPath);
//            file.createNewFile();
//            OutputStream outputStream = new FileOutputStream(file);
//            outputStream.write(picture.getBytes());
//
//            bouquet.setUrl(imagesPath);
//        }
//        catch (IOException e) {
//            System.out.println(e.getStackTrace());
//        }




//    }


}
