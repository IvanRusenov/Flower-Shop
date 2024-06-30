package com.ivan.Flowers.Shop.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadImage(MultipartFile multipartFile) throws IOException;
    boolean deleteImage(String imageUrl) throws IOException;
}

