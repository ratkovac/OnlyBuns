package com.group27.OnlyBuns.loadbalancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoadBalancer {

    private static final Logger log = LoggerFactory.getLogger(LoadBalancer.class);

    @Value("#{'${loadbalancer.service.urls}'.split(',')}")
    private List<String> serviceUrls;

    private final RestTemplate restTemplate = new RestTemplate();
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public String forwardRequest(String path) {
        if (serviceUrls == null || serviceUrls.isEmpty()) {
            return "Greška: Nisu konfigurisane adrese servisa.";
        }

        int retries = serviceUrls.size();
        for (int i = 0; i < retries; i++) {
            String baseUrl = getNextUrl();
            String fullUrl = baseUrl + path;
            try {
                log.info("Prosleđujem zahtev na instancu: {}", fullUrl);
                return restTemplate.getForObject(fullUrl, String.class);
            } catch (Exception e) {
                log.warn("Instanca ne odgovara: {}. Pokušavam sledeću...", baseUrl);
            }
        }

        log.error("Greška: Nijedna instanca nije dostupna nakon {} pokušaja.", retries);
        return "Greška: Nijedna instanca nije dostupna.";
    }

    private String getNextUrl() {
        int index = currentIndex.getAndUpdate(i -> (i + 1) % serviceUrls.size());
        return serviceUrls.get(index);
    }

    public String forwardPostRequest(String path, Object body) {
        if (serviceUrls == null || serviceUrls.isEmpty()) {
            return "Greška: Nisu konfigurisane adrese servisa.";
        }

        int retries = serviceUrls.size();
        for (int i = 0; i < retries; i++) {
            String baseUrl = getNextUrl();
            String fullUrl = baseUrl + path;
            try {
                log.info("Prosleđujem POST zahtev na instancu: {}", fullUrl);
                return restTemplate.postForObject(fullUrl, body, String.class);
            } catch (Exception e) {
                log.warn("Instanca ne odgovara: {}. Pokušavam sledeću...", baseUrl);
            }
        }

        log.error("Greška: Nijedna instanca nije dostupna nakon {} pokušaja.", retries);
        return "Greška: Nijedna instanca nije dostupna.";
    }
}
