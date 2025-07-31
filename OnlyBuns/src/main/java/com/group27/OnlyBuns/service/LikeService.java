package com.group27.OnlyBuns.service;


import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.repository.LikeRepository;
import com.group27.OnlyBuns.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    private final LikeRepository likeRepository;

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    // Metoda za proveru da li je korisnik lajkovao objavu
    public boolean isPostLikedByUser(Long postId, Long userId) {
        Optional<Like> like = likeRepository.findByPostIdAndUserId(postId, userId);
        return like.isPresent();  // Ako postoji like, korisnik je lajkovao
    }

    // Ostale metode za lajkovanje, brisanje lajka, itd.


    @Cacheable(value = "pupularPostsLast7Days", key = "#root.methodName")
    public List<Long> getTop5LikedPostIdsInLast7Days() {
        List<Like> allLikes = likeRepository.findAll();
        System.out.println("Učitani su svi lajkovi \n \n");

        // Pronađi lajkove u poslednjih 7 dana
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Like> recentLikes = allLikes.stream()
                .filter(like -> like.getCreatedAt().isAfter(sevenDaysAgo))
                .collect(Collectors.toList());
        System.out.println("Učitani su recent lajkovi \n \n");

        // Grupisanje lajkova po postId i brojanje
        Map<Long, Long> likeCounts = recentLikes.stream()
                .collect(Collectors.groupingBy(Like::getPostId, Collectors.counting()));
        System.out.println("Lajkovi su mapirani za objave \n \n");

        // Dohvati top 5 postova na osnovu lajkova
        List<Long> topPostIds = likeCounts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Silazno sortiranje
                .limit(5) // Uzimanje top 5
                .map(Map.Entry::getKey) // Izvlačenje postId-jeva
                .collect(Collectors.toList());

        // Provera da li već imamo 5 postova
        if (topPostIds.size() < 5) {
            // Dohvati sve postove
            List<Long> allPostIds = postRepository.findAll().stream()
                    .map(Post::getId)
                    .collect(Collectors.toList());

            // Filtriraj ID-jeve postova koji nisu već u listi topPostIds
            List<Long> zeroLikePostIds = allPostIds.stream()
                    .filter(postId -> !likeCounts.containsKey(postId)) // Postovi sa 0 lajkova
                    .filter(postId -> !topPostIds.contains(postId)) // Izbegavanje duplikata
                    .collect(Collectors.toList());

            // Nasumično permutuj listu postova sa 0 lajkova
            Collections.shuffle(zeroLikePostIds);

            // Dodaj postove sa 0 lajkova u listu dok ne dostignemo 5 elemenata
            for (Long postId : zeroLikePostIds) {
                if (topPostIds.size() < 5) {
                    topPostIds.add(postId);
                } else {
                    break;
                }
            }
        }

        return topPostIds;
    }

    @Cacheable(value = "pupularPostsAllTime", key = "#root.methodName")
    public List<Long> getTop10LikedPostIdsAllTime() {
        List<Like> allLikes = likeRepository.findAll();

        // Grupisanje lajkova po postId i brojanje
        Map<Long, Long> likeCounts = allLikes.stream()
                .collect(Collectors.groupingBy(Like::getPostId, Collectors.counting()));

        // Dohvatanje top 10 postova na osnovu lajkova
        List<Long> topPostIds = likeCounts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Silazno sortiranje
                .limit(10) // Uzimanje top 10
                .map(Map.Entry::getKey) // Izvlačenje postId-jeva
                .collect(Collectors.toList());

        // Provera da li već imamo 10 postova
        if (topPostIds.size() < 10) {
            // Dohvati sve postove
            List<Long> allPostIds = postRepository.findAll().stream()
                    .map(Post::getId)
                    .collect(Collectors.toList());

            // Filtriraj ID-jeve postova koji nisu već u listi topPostIds
            List<Long> zeroLikePostIds = allPostIds.stream()
                    .filter(postId -> !likeCounts.containsKey(postId)) // Postovi sa 0 lajkova
                    .filter(postId -> !topPostIds.contains(postId)) // Izbegavanje duplikata
                    .collect(Collectors.toList());

            // Nasumično permutuj listu postova sa 0 lajkova
            Collections.shuffle(zeroLikePostIds);

            // Dodaj postove sa 0 lajkova u listu dok ne dostignemo 10 elemenata
            for (Long postId : zeroLikePostIds) {
                if (topPostIds.size() < 10) {
                    topPostIds.add(postId);
                } else {
                    break;
                }
            }
        }

        return topPostIds;
    }

    @Cacheable(value = "pupularUsersLast7Days", key = "#root.methodName")
    public List<Long> getMostActiveUserIdsInLast7Days() {
        List<Like> allLikes = likeRepository.findAll();

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Like> recentLikes = allLikes.stream()
                .filter(like -> like.getCreatedAt().isAfter(sevenDaysAgo))
                .collect(Collectors.toList());

        Map<Long, Long> likeCounts = recentLikes.stream()
                .collect(Collectors.groupingBy(Like::getUserId, Collectors.counting()));

        return likeCounts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Silazno sortiranje
                .limit(10) // Uzimanje top 5
                .map(Map.Entry::getKey) // Izvlačenje postId-jeva
                .collect(Collectors.toList());
    }
}