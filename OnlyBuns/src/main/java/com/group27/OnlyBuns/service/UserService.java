package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.repository.PostRepository;
import com.group27.OnlyBuns.repository.UserFollowerRepository;
import com.group27.OnlyBuns.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private UserFollowerRepository userFollowerRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Kreiranje novog korisnika
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Pronalazak korisnika po korisničkom imenu
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User checkUser(String username, String password){
        User user = getUserByUsername(username);
        System.out.println("User ucitan");
        if(user.getPassword().equals(password)){
            System.out.println("Tacna sifra");
            return user;
        }
        System.out.println("Pogresna sifra");
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

    public List<User> searchUsers(String firstName, String lastName, String email, Long minPosts, Long maxPosts) {
        return userRepository.findUsersByCriteria(firstName, lastName, email, minPosts, maxPosts);
    }

    public List<User> findUsersSortedByFollowingCount(String sortDirection) {
        if (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Invalid sort direction. Use 'ASC' or 'DESC'.");
        }
        return userRepository.findUsersSortedByFollowingCount(sortDirection);
    }

    public List<User> findUsersSortedByEmail(String sortDirection) {
        if (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("Invalid sort direction. Use 'ASC' or 'DESC'.");
        }
        return userRepository.findUsersSortedByEmail(sortDirection);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getRole().equals("admin")) // Filtrira korisnike čija je uloga 'admin'
                .collect(Collectors.toList());
    }
}
