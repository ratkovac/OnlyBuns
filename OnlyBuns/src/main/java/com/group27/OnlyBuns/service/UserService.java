package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.model.UserFollower;
import com.group27.OnlyBuns.repository.*;
import com.group27.OnlyBuns.model.VerificationToken;
import com.group27.OnlyBuns.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<Long, List<Long>> followTimestamps = new HashMap<>();
    private final int MAX_FOLLOWS_PER_MINUTE = 50;
    private final long TIME_WINDOW_MILLIS = 60_000;

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserFollowerRepository userFollowerRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private WebServerApplicationContext serverAppContext;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    // Kreiranje novog korisnika
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Pronalazak korisnika po korisničkom imenu
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User checkUser(String username, String password) {
        User user = getUserByUsername(username);
        System.out.println("User ucitan");
        if (user.isActive()) {
            if (user.getPassword().equals(password)) {
                System.out.println("Tacna sifra");
                return user;
            }
            System.out.println("Pogresna sifra");
        }else{
            System.out.println("Korisnik nije verifikovan");
        }
        return null;
    }

    public long countUsersFollowedBy(Long userId) {
        return userFollowerRepository.countByFollowerId(userId);
    }

    public long countPosts(Long userId) {
        return postRepository.countByUserId(userId);
    }

//    public Page<User> getUsersPage(int pageNumber, int pageSize) {
//        return userRepository.findAll(PageRequest.of(pageNumber, pageSize));
//    }

    public Page<User> searchUsers(String firstName, String lastName, String email, String username, Long minPosts, Long maxPosts, Pageable pageable) {
        return userRepository.findUsersByCriteria(firstName, lastName, email, username, minPosts, maxPosts, pageable);
    }


    public Page<User> findUsersSortedByFollowingCount(String sortDirection, Pageable pageable) {
        if (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Invalid sort direction. Use 'ASC' or 'DESC'.");
        }
        return userRepository.findUsersSortedByFollowingCount(sortDirection, pageable);
    }


    public Page<User> findUsersSortedByEmail(String sortDirection, Pageable pageable) {
        if (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Invalid sort direction. Use 'ASC' or 'DESC'.");
        }
        return userRepository.findUsersSortedByEmail(sortDirection, pageable);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        int port = serverAppContext.getWebServer().getPort();
        System.out.println("Poziv getAllPosts() na portu: " + port);
        return userRepository.findAllNonAdminUsers(pageable);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User registerUser(User user) {
        // Simulacija sporog pristupa za testiranje konflikata
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (getUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Korisnicko ime vec postoji");
        }

        if (getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email vec postoji");
        }

        Long maxId = userRepository.findMaxId();
        if (maxId != null) {
            System.out.println(maxId);
            user.setId(maxId + 1);
        } else {
            user.setId(1L);
            System.out.println("Ne nadje maxId");
        }


        user.setActive(false);
        user.setRole("user");
        return createUser(user);
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findById(id));
    }

    public String logIn(String username, String password) {
        User user = getUserByUsername(username);
        System.out.println("User ucitan");
        if (user.isActive()) {
            if (user.getPassword().equals(password)) {
                System.out.println("Tacna sifra");
                return jwtUtil.generateToken(user);
                //return jwtUtil.generateToken(username, user.getId(), user.getRole());
            }
            System.out.println("Pogresna sifra");
        }else{
            System.out.println("Korisnik nije verifikovan");
        }
        return null;
    }

    public User verify(long userId) {
        User user = userRepository.getUsersById(userId);
        user.setActive(true);
        userRepository.save(user);
        return user;
    }

    public List<User> usersFollowersBy(Long userId) {
        List<User> followers = new ArrayList<User>();
        List<UserFollower> userFollowers = userFollowerRepository.getAllByFolloweeId(userId);

        for(UserFollower follower : userFollowers){
            User user = follower.getFollower();
            followers.add(user);
        }

        return followers;
    }

    public List<User> usersFollowedBy(Long userId) {
        List<User> followed = new ArrayList<>();
        List<UserFollower> userFollowers = userFollowerRepository.getAllByFollowerId(userId);

        for(UserFollower follower : userFollowers){
            User user = follower.getFollowee();
            followed.add(user);
        }

        return followed;
    }

    @Scheduled(cron = "0 0 0 L * ?") // Pokreće se u ponoć poslednjeg dana u mesecu
    public void deleteInactiveUsers() {
        List<User> inactiveUsers = userRepository.findInactiveUsers();
        inactiveUsers.forEach(user -> {
            System.out.println("Deleting inactive user: " + user.getUsername());
            userRepository.delete(user);
        });
    }

    @Transactional
    public synchronized boolean followUser(Long followerId, Long followeeId) {
        if (Objects.equals(followerId, followeeId)) return false;

        if (!canFollow(followerId)) {
            throw new RuntimeException("Prekoračen limit praćenja (max 50 puta u minutu)");
        }

        Optional<User> followerOpt = userRepository.findById(followerId);
        Optional<User> followeeOpt = userRepository.findById(followeeId);

        if (followerOpt.isEmpty() || followeeOpt.isEmpty()) return false;

        if (userFollowerRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            return false;
        }

        // Simulacija sporog pristupa (konkurentni test)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        UserFollower userFollower = new UserFollower();
        userFollower.setFollower(followerOpt.get());
        userFollower.setFollowee(followeeOpt.get());

        userFollowerRepository.save(userFollower);
        return true;
    }

    @Transactional
    public boolean unfollowUser(Long followerId, Long followeeId) {
        System.out.println("Trying to unfollow: " + followerId + " -> " + followeeId);

        if (Objects.equals(followerId, followeeId)) {
            return false;
        }

        boolean exists = userFollowerRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
        System.out.println("Exists? " + exists);

        if (!exists) {
            System.out.println("User was not following.");
            return false;
        }

        userFollowerRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
        System.out.println("Deleted follow entry.");
        return true;
    }

    public boolean isFollowing(Long followerId, Long followeeId) {
        return userFollowerRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    private synchronized boolean canFollow(Long userId) {
        long now = System.currentTimeMillis();

        List<Long> timestamps = followTimestamps.getOrDefault(userId, new ArrayList<>());

        timestamps.removeIf(timestamp -> now - timestamp > TIME_WINDOW_MILLIS);

        if (timestamps.size() >= MAX_FOLLOWS_PER_MINUTE) {
            return false;
        }

        timestamps.add(now);
        followTimestamps.put(userId, timestamps);
        return true;
    }

    public Map<String, Long> getUserEngagementStats() {
        List<User> allUsers = userRepository.findAll();
        long total = allUsers.size();

        long posted = allUsers.stream()
                .filter(user -> postRepository.countByUserId(user.getId()) > 0)
                .count();

        long commented = allUsers.stream()
                .filter(user -> commentRepository.countByUserId(user.getId()) > 0)
                .filter(user -> postRepository.countByUserId(user.getId()) == 0) // samo komentarisali
                .count();

        long inactive = total - posted - commented;

        Map<String, Long> stats = new HashMap<>();
        stats.put("posted", posted);
        stats.put("commented", commented);
        stats.put("inactive", inactive);
        stats.put("total", total);

        return stats;
    }

    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }
}
