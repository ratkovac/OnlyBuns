package com.group27.OnlyBuns.ServiceTest;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.repository.UserRepository;
import com.group27.OnlyBuns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestConcurentRegistration {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testConcurrentRegistration() throws InterruptedException {
        long initialUserCount = userRepository.count();

        // Kreiranje ExecutorService za konkurentno izvršavanje
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Kreiranje korisnika sa istim podacima
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setEmail("testuser@example.com");
        user1.setPassword("Password123!");
        user1.setAddress("address");
        user1.setFirstName("Firstname");
        user1.setLastName("Lastname");


        User user2 = new User();
        user2.setUsername("testuser"); // Isto korisničko ime kao user1
        user2.setEmail("testuser@example.com"); // Isto email kao user1
        user2.setPassword("Password123!");
        user2.setAddress("address");
        user2.setFirstName("Firstname");
        user2.setLastName("Lastname");


        // Pokretanje konkurentnih niti
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

        // Zatvaranje executor-a
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        // Proveri da li je samo jedan korisnik registrovan
        long finalUserCount = userRepository.count();
        assertThat(finalUserCount).isEqualTo(initialUserCount + 1);

        // Proveri da li je korisnik sa korisničkim imenom "testuser" uspešno registrovan
        assertThat(userRepository.findByUsername("testuser")).isNotNull();

        userRepository.delete(userService.getUserByUsername(user1.getUsername()));
    }
}
