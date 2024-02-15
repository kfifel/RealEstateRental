package com.fil.rouge.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtils {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        // Generate a unique filename
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Create the upload directory if it doesn't exist
        Path directory = Paths.get(uploadDir);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Save the file to the upload directory
        Path filePath = directory.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    public boolean deleteFile(String fileName) throws IOException {
        // Resolve the file path
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        // Delete the file
        return Files.deleteIfExists(filePath);
    }

    public File getFile(String fileName) {
        // Resolve the file path
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        // Create a File object
        return filePath.toFile();
    }
}
