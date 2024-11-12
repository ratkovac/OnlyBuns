package com.group27.OnlyBuns.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageService {

    @Value("${image.upload-dir}")
    private String imageDir;

    @Value("${image.compressed-dir}")
    private String compressedDir;

    @Scheduled(cron = "0 16 22 * * ?")
    public void compressOldImages() {
        System.out.println("Hello, World!");
        File dir = new File(imageDir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                long diffInMillies = new Date().getTime() - file.lastModified();
                long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);

                if (diffInDays > 30) {
                    try {
                        File compressedDirectory = new File(compressedDir);
                        if (!compressedDirectory.exists()) {
                            compressedDirectory.mkdirs();
                        }

                        File compressedFile = new File(compressedDirectory, file.getName());

                        Thumbnails.of(file)
                                .size(800, 800)
                                .outputFormat("jpg")
                                .outputQuality(0.75)
                                .toFile(compressedFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
