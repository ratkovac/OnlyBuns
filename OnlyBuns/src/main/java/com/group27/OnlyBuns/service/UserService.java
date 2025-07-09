package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.model.UserFollower;
import com.group27.OnlyBuns.repository.PostRepository;
import com.group27.OnlyBuns.repository.UserFollowerRepository;
import com.group27.OnlyBuns.repository.VerificationTokenRepository;
import com.group27.OnlyBuns.model.VerificationToken;
import com.group27.OnlyBuns.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
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
        return userRepository.findAllNonAdminUsers(pageable);
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
                return generateJWT(username, user.getId(), user.getRole());
            }
            System.out.println("Pogresna sifra");
        }else{
            System.out.println("Korisnik nije verifikovan");
        }
        return null;
    }

    private static final String SECRET_KEY = "9lA8q1tUjKTx1mX2LdKvQ7fV2pNc5wQ6R2p3MmN8P1A=";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 2 sata

    public static String generateJWT(String username, Long userId, String role) {
        return Jwts.builder()
                .setSubject(username) // sub: korisničko ime
                .claim("id", userId)   // Dodaj ID korisnika kao claim
                .claim("role", role)   // Dodaj ulogu korisnika kao claim
                .setIssuedAt(new Date()) // Datum izdavanja
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Rok trajanja
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Potpisivanje sa tajnim ključem
                .compact();
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

        Optional<User> followerOpt = userRepository.findById(followerId);
        Optional<User> followeeOpt = userRepository.findById(followeeId);

        if (followerOpt.isEmpty() || followeeOpt.isEmpty()) return false;

        if (userFollowerRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            return false;
        }

        // Simulacija sporog pristupa (konkurentni test)
        try {
            Thread.sleep(500); // <-- testiranje konkurencije
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

        // Očisti stare unose (starije od 1 minuta)
        timestamps.removeIf(timestamp -> now - timestamp > TIME_WINDOW_MILLIS);

        if (timestamps.size() >= MAX_FOLLOWS_PER_MINUTE) {
            return false; // Prešao limit
        }

        // Dodaj trenutni timestamp i sačuvaj
        timestamps.add(now);
        followTimestamps.put(userId, timestamps);
        return true;
    }
}
