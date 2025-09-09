package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.model.VerificationToken;
import com.group27.OnlyBuns.service.EmailSenderService;
import com.group27.OnlyBuns.service.UserService;
import com.group27.OnlyBuns.utils.SimpleRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    private final EmailSenderService emailSenderService;

    private SimpleRateLimiter rateLimiter;

    @Autowired
    public UserController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.rateLimiter = new SimpleRateLimiter(5, 60000);
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

    /*@PostMapping("/login")
    public User checkUser(@RequestBody User user) {
        return userService.checkUser(user.getUsername(), user.getPassword());
    }*/


    @PostMapping("/login")
    public String logIn(@RequestBody User user, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr(); // Dobijanje IP adrese
        if(rateLimiter.allowRequest(clientIp)) {
            String response = userService.logIn(user.getUsername(), user.getPassword());
            if(response != null){
                User loggedUser = userService.getUserByUsername(user.getUsername());
                System.out.println("Login2: " + loggedUser.getUsername() + loggedUser.getAddress() + loggedUser.getFirstName());
                loggedUser.setLastLoginTime(LocalDateTime.now());
                System.out.println("Login3: " + loggedUser.getLastLoginTime());
                userService.updateUser(loggedUser);
            }
            return response;
        }else{
            return "Previse puta je pokusana sifra";
        }
    }

    @GetMapping("/{userId}/following/count")
    public long getCountOfUsersFollowed(@PathVariable Long userId) {
        return userService.countUsersFollowedBy(userId);
    }

    @GetMapping("/followers/{userId}")
    public List<User> getUsersFollowed(@PathVariable Long userId) {
        return userService.usersFollowersBy(userId);
    }

    @GetMapping("/following/{userId}")
    public List<User> getUsersFollowing(@PathVariable Long userId) {
        return userService.usersFollowedBy(userId);
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
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long minPosts,
            @RequestParam(required = false) Long maxPosts,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchUsers(firstName, lastName, email, username, minPosts, maxPosts, pageable);
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

        String verificationLink = "http://localhost:4200/verificationMail/" + regUser.getId();

        emailSenderService.sendEmail(
                user.getEmail(),
                "Verifikacija OnlyBuns profila",
                "Kliknite na sledeći link da biste verifikovali vaš nalog: " + verificationLink
        );

        return regUser;
    }

    @PostMapping("/verify/{userId}")
    public User verifyUser(@PathVariable long userId) {
        return userService.verify(userId);
    }

    @DeleteMapping("/inactive")
    public ResponseEntity<String> deleteInactiveUsers() {
        userService.deleteInactiveUsers();
        return ResponseEntity.ok("Inactive users deleted successfully");
    }

    @GetMapping("/{currentUserId}/isFollowing/{targetUserId}")
    public ResponseEntity<Map<String, Boolean>> isFollowing(@PathVariable Long currentUserId, @PathVariable Long targetUserId) {
        boolean isFollowing = userService.isFollowing(currentUserId, targetUserId);
        return ResponseEntity.ok(Collections.singletonMap("isFollowing", isFollowing));
    }

    @PostMapping("/{currentUserId}/follow/{targetUserId}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable Long currentUserId, @PathVariable Long targetUserId) {
        try {
            userService.followUser(currentUserId, targetUserId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Followed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(429).body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "Došlo je do greške na serveru."));
        }
    }

    @PostMapping("/{currentUserId}/unfollow/{targetUserId}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable Long currentUserId, @PathVariable Long targetUserId) {
        try {
            userService.unfollowUser(currentUserId, targetUserId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Unfollowed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "Došlo je do greške na serveru."));
        }
    }

    @GetMapping("/engagementStats")
    public ResponseEntity<Map<String, Long>> getUserEngagementStats() {
        return ResponseEntity.ok(userService.getUserEngagementStats());
    }
}

