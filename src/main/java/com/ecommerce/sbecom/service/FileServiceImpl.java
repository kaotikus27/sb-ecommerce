package com.ecommerce.sbecom.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Get the filenames of the current / original file
        String originalFilename = file.getOriginalFilename();

        // Generate a unique file name -- using UUID
        String randomId = UUID.randomUUID().toString();

        //leo.jpg --> 1234 --> 1234.jpg
        String fileName = randomId
                .concat(originalFilename
                        .substring(originalFilename.lastIndexOf('.')));

        // initializing path
        String filePath = path + File.separator + fileName;

        // Check if path exist and create if not create new
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdirs();

        // Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));


        // Returning file name
        return fileName;

    }
}
