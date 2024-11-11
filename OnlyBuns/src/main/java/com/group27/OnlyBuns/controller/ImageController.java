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

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Value("${image.upload-dir}")
    private String imageDir;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            File dir = new File(imageDir);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to create directory: " + imageDir);
                }
            }

            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body("No file selected for upload.");
            }

            String filePath = Paths.get(imageDir, image.getOriginalFilename()).toString();
            File imageFile = new File(filePath);

            image.transferTo(imageFile.getAbsoluteFile());

            return ResponseEntity.ok("Image uploaded successfully: " + imageFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/test-compress")
    public String testCompress() {
        imageService.compressOldImages();
        return "Image compression started.";
    }
}
