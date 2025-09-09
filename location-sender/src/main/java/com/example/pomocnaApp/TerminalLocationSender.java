package com.example.pomocnaApp;

import com.example.pomocnaApp.dtos.LocationDto;
import com.example.pomocnaApp.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Scanner;

@Component
public class TerminalLocationSender implements CommandLineRunner {

    private final LocationService locationService;

    @Autowired
    public TerminalLocationSender(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        while (true) {
            System.out.println("\nDa li želite da pošaljete lokaciju? [DA/ne]");
            String odgovor = scanner.nextLine();

            if (odgovor.equalsIgnoreCase("ne")) {
                break;
            }

            try {
                double latitude;
                double longitude;

                System.out.print("Unesite geografsku širinu (latitude) [Enter za random]: ");
                String latInput = scanner.nextLine();

                if (latInput.isEmpty()) {
                    latitude = -90 + (180 * random.nextDouble());
                    System.out.println("-> Korišćena random vrednost: " + latitude);
                } else {
                    latitude = Double.parseDouble(latInput);
                }

                System.out.print("Unesite geografsku dužinu (longitude) [Enter za random]: ");
                String lonInput = scanner.nextLine();

                if (lonInput.isEmpty()) {
                    longitude = -180 + (360 * random.nextDouble());
                    System.out.println("-> Korišćena random vrednost: " + longitude);
                } else {
                    longitude = Double.parseDouble(lonInput);
                }

                LocationDto locationDto = new LocationDto();
                locationDto.setLatitude(latitude);
                locationDto.setLongitude(longitude);

                locationService.sendLocation(locationDto);
                System.out.println(">>> Uspeh! Lokacija je poslata na ActiveMQ.");

            } catch (NumberFormatException e) {
                System.err.println("!!! Greška: Uneta vrednost nije validan broj. Molimo pokušajte ponovo.");
            } catch (Exception e) {
                System.err.println("!!! Greška prilikom slanja lokacije: " + e.getMessage());
            }
        }

        System.out.println("Aplikacija se gasi. Doviđenja!");
    }
}