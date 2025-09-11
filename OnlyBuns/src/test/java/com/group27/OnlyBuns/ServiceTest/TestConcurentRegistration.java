package com.group27.OnlyBuns.ServiceTest;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.repository.UserRepository;
import com.group27.OnlyBuns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestConcurentRegistration {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testConcurrentRegistration() throws InterruptedException {
        long initialUserCount = userRepository.count();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        User user1 = new User();
        user1.setUsername("testuser");
        user1.setEmail("testuser@example.com");
        user1.setPassword("Password123!");
        user1.setAddress("address");
        user1.setFirstName("Firstname");
        user1.setLastName("Lastname");
        user1.setLastLoginTime(LocalDateTime.now());


        User user2 = new User();
        user2.setUsername("testuser");
        user2.setEmail("testuser@example.com");
        user2.setPassword("Password123!");
        user2.setAddress("address");
        user2.setFirstName("Firstname");
        user2.setLastName("Lastname");
        user1.setLastLoginTime(LocalDateTime.now());


        Runnable task1 = () -> {
            try {
                User user11 = userService.registerUser(user1);
            } catch (Exception e) {
                System.out.println("Task1: " + e.getMessage());
            }
        };

        Runnable task2 = () -> {
            try {
                User user12 =userService.registerUser(user2);
            } catch (Exception e) {
                System.out.println("Task2: " + e.getMessage());
            }
        };

        executor.submit(task1);
        executor.submit(task2);

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        long finalUserCount = userRepository.count();
        assertThat(finalUserCount).isEqualTo(initialUserCount + 1);

        assertThat(userRepository.findByUsername("testuser")).isNotNull();

        userRepository.delete(userService.getUserByUsername(user1.getUsername()));
    }
}
