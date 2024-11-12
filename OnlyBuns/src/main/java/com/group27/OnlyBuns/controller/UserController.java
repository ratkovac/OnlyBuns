package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.model.VerificationToken;
import com.group27.OnlyBuns.service.EmailSenderService;
import com.group27.OnlyBuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    private final EmailSenderService emailSenderService;

    @Autowired
    public UserController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }


    // Endpoint za kreiranje novog korisnika
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Endpoint za dobijanje korisnika po korisničkom imenu
    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/getById/{id}")
    public Optional<User> getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }


    @PostMapping("/login")
    public User checkUser(@RequestBody User user) {
        return userService.checkUser(user.getUsername(), user.getPassword());
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        User regUser = userService.registerUser(user);

        VerificationToken vt = userService.saveToken(regUser);
        emailSenderService.sendEmail("nik.letvencuk@gmail.com", "Verifikacija OnlyBuns profila","Vas kod za verifikaciju je "+ vt.getCode());
        return regUser;
    }

    @PostMapping("/verify")
    public VerificationToken verifyUser(@RequestBody VerificationToken verificationToken) {
        return userService.verifyToken(verificationToken);
    }

}
