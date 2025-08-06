package com.group27.OnlyBuns.loadbalancer;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoadBalancer {

    private final List<String> serviceUrls = List.of(
            "http://localhost:8081",
            "http://localhost:8082"
    );

    private final RestTemplate restTemplate = new RestTemplate();
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public String getPostsFromBalancedInstance() {
        int retries = serviceUrls.size();

        for (int i = 0; i < retries; i++) {
            String baseUrl = getNextUrl();
            try {
                return restTemplate.getForObject(baseUrl + "/users", String.class);
            } catch (Exception e) {
                System.out.println("Instanca ne odgovara: " + baseUrl + ", pokušavam sledeću...");
            }
        }

        return "Greška: nijedna instanca nije dostupna.";
    }

    private String getNextUrl() {
        int index = currentIndex.getAndUpdate(i -> (i + 1) % serviceUrls.size());
        return serviceUrls.get(index);
    }
}
