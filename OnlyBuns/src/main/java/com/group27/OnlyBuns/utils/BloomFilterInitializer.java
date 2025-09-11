package com.group27.OnlyBuns.utils;

import com.google.common.hash.BloomFilter;
import com.group27.OnlyBuns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BloomFilterInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BloomFilter<String> usernameBloomFilter;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Popunjavanje Bloom Filtera sa korisničkim imenima...");
        List<String> allUsernames = userRepository.findAllUsernames(); // Pretpostavka da imate ovakvu metodu
        allUsernames.forEach(usernameBloomFilter::put);
        System.out.println("Bloom Filter je napunjen.");
    }
}