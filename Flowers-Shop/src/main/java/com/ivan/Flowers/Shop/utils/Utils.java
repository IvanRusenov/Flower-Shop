package com.ivan.Flowers.Shop.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Utils {


    public static final String UPLOAD_FOLDER = ".\\uploads\\";

    public static void uploadFile(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_FOLDER);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("File " + file.getOriginalFilename() + " was not saved!!!", e);
        }
    }


}
