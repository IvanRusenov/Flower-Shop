//package com.ivan.Flowers.Shop.services.impls;
//
//import com.ivan.Flowers.Shop.services.CloudinaryService;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//
//public class CloudinaryServiceImpl implements CloudinaryService {
//
//    private final Cloudinary cloudinary;
//
//    public CloudinaryServiceImpl(Cloudinary cloudinary) {
//        this.cloudinary = cloudinary;
//    }
//
//    @Override
//    public String uploadImage(MultipartFile multipartFile) throws IOException {
//        File file = File
//                .createTempFile("temp-file", multipartFile.getOriginalFilename());
//        multipartFile.transferTo(file);
//
//        return this.cloudinary.uploader()
//                .upload(file, new HashMap())
//                .get("url").toString();
//    }
//}
