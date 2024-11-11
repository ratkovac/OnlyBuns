package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

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

    // Dodajte ostale metode po potrebi
}
