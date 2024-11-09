package com.group27.OnlyBuns;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class WelcomeController {

    @GetMapping("/welcome")
    String welcome() {
        return "Welcome to Only Buns";
    }
}
