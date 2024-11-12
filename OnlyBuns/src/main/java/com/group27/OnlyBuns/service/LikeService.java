package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    // Metoda za proveru da li je korisnik lajkovao objavu
    public boolean isPostLikedByUser(Long postId, Long userId) {
        Optional<Like> like = likeRepository.findByPostIdAndUserId(postId, userId);
        return like.isPresent();  // Ako postoji like, korisnik je lajkovao
    }

    // Ostale metode za lajkovanje, brisanje lajka, itd.
}