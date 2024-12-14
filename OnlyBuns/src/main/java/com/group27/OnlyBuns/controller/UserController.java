package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.model.VerificationToken;
import com.group27.OnlyBuns.service.EmailSenderService;
import com.group27.OnlyBuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/{userId}/following/count")
    public long getCountOfUsersFollowed(@PathVariable Long userId) {
        return userService.countUsersFollowedBy(userId);
    }

    @GetMapping("/{userId}/posts/count")
    public long getPostsCount(@PathVariable Long userId) {
        return userService.countPosts(userId);
    }

    // Pretraga korisnika sa podrškom za paginaciju
    @GetMapping("/search")
    public Page<User> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long minPosts,
            @RequestParam(required = false) Long maxPosts,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchUsers(firstName, lastName, email, minPosts, maxPosts, pageable);
    }

    // Sortiranje korisnika po broju praćenja sa podrškom za paginaciju
    @GetMapping("/sort/following")
    public Page<User> sortUsersByFollowingCount(
            @RequestParam String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findUsersSortedByFollowingCount(sortDirection, pageable);
    }

    // Sortiranje korisnika po emailu sa podrškom za paginaciju
    @GetMapping("/sort/email")
    public Page<User> sortUsersByEmail(
            @RequestParam String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findUsersSortedByEmail(sortDirection, pageable);
    }

    // Dobijanje svih korisnika sa podrškom za paginaciju
    @GetMapping
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        User regUser = userService.registerUser(user);

        VerificationToken vt = userService.saveToken(regUser);
        emailSenderService.sendEmail(user.getEmail(), "Verifikacija OnlyBuns profila", "Vas kod za verifikaciju je " + vt.getCode());
        return regUser;
    }

    @PostMapping("/verify")
    public VerificationToken verifyUser(@RequestBody VerificationToken verificationToken) {
        return userService.verifyToken(verificationToken);
    }
}

