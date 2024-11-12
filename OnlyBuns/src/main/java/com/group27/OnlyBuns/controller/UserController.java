package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long minPosts,
            @RequestParam(required = false) Long maxPosts) {
        return userService.searchUsers(firstName, lastName, email, minPosts, maxPosts);
    }

    @GetMapping("/sort/following")
    public List<User> sortUsersByFollowingCount(@RequestParam String sortDirection) {
        return userService.findUsersSortedByFollowingCount(sortDirection);
    }

    @GetMapping("/sort/email")
    public List<User> sortUsersByEmail(@RequestParam String sortDirection) {
        return userService.findUsersSortedByEmail(sortDirection);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
