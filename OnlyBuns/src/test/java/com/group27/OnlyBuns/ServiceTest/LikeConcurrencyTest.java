package com.group27.OnlyBuns.ServiceTest;

import com.group27.OnlyBuns.service.LikeService;
import com.group27.OnlyBuns.service.PostService;
import com.group27.OnlyBuns.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LikeConcurrencyTest {

    @Autowired
    private PostService postService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    LikeService likeService;

    @Test
    public void testConcurrentLikes() throws InterruptedException {
        Long postId = 1L;

        long initialLikes = likeRepository.countByPostId(postId);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            postService.addLike(postId, 101L);
        };

        Runnable task2 = () -> {
            try {
                Thread.sleep(50);
                postService.addLike(postId, 102L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        executor.submit(task1);
        executor.submit(task2);

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        long finalLikes = likeRepository.countByPostId(postId);
        System.out.println("Initial likes: " + initialLikes);
        System.out.println("Final likes: " + finalLikes);

        assertEquals(initialLikes + 2, finalLikes);

        likeService.deleteLike(postId, 101L);
        likeService.deleteLike(postId, 102L);
    }
}