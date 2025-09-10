package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

@Service
public class QueueService {

    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
    private final Path queueDirectory = Paths.get("queues");

    public QueueService() {
        try {
            if (!Files.exists(queueDirectory)) {
                Files.createDirectories(queueDirectory);
                logger.info("Created queue directory at: {}", queueDirectory.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Could not create queue directory", e);
        }
    }

    public synchronized void sendMessage(String queueName, String message) throws IOException {
        Path queueFile = queueDirectory.resolve(queueName + ".txt");
        Files.writeString(queueFile, message + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        logger.info("Message added to queue '{}'", queueName);
    }

    public synchronized Optional<String> receiveMessage(String queueName) throws IOException {
        Path queueFile = queueDirectory.resolve(queueName + ".txt");
        if (!Files.exists(queueFile)) {
            return Optional.empty();
        }

        List<String> lines = Files.readAllLines(queueFile);
        if (lines.isEmpty()) {
            return Optional.empty();
        }

        String message = lines.remove(0);

        Files.write(queueFile, lines);

        logger.info("Message consumed from queue '{}'", queueName);
        return Optional.of(message);
    }
}