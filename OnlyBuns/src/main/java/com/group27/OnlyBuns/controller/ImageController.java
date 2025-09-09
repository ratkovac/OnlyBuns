package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Value("${image.upload-dir}")
    private String imageDir;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            File dir = new File(imageDir);
            if (!dir.exists() && !dir.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to create directory: " + imageDir));
            }

            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "No file selected for upload."));
            }

            // Očisti ime fajla – zameni razmake
            String fileName = image.getOriginalFilename().replaceAll("\\s+", "_");
            String filePath = Paths.get(imageDir, fileName).toString();
            File imageFile = new File(filePath);
            System.out.println("Saving image to: " + imageFile.getAbsolutePath()); // DEBUG

            image.transferTo(imageFile.getAbsoluteFile());

            // Pravi URL koji će frontend koristiti
            String imageUrl = "/images/originals/" + fileName;

            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }



    @GetMapping("/test-compress")
    public String testCompress() {
        imageService.compressOldImages();
        return "Image compression started.";
    }
}