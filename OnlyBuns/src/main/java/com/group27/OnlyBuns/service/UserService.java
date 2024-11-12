package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.model.VerificationToken;
import com.group27.OnlyBuns.repository.UserRepository;
import com.group27.OnlyBuns.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

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
    // Dodajte ostale metode po potrebi

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(User user) {
        if (getUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Korisničko ime već postoji");
        }

        if (getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email već postoji");
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

    public VerificationToken saveToken(User user){
        VerificationToken verificationToken = new VerificationToken(user.getId(), generateRandomString());
        return verificationTokenRepository.save(verificationToken);
    }

    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    public VerificationToken verifyToken(VerificationToken verificationToken) {
        VerificationToken token = verificationTokenRepository.findByUserId(verificationToken.getUserId());
        System.out.println(token.getId());
        if(verificationToken.getCode().equals(token.getCode())){
            User user = userRepository.getUsersById(verificationToken.getUserId());
            user.setActive(true);
            userRepository.save(user);
            verificationTokenRepository.delete(token);
            return verificationToken;
        }
        return null;
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findById(id));
    }
}
