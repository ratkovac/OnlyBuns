package com.group27.OnlyBuns.ServiceTest;

import com.group27.OnlyBuns.model.Like;
import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.repository.LikeRepository;
import com.group27.OnlyBuns.repository.PostRepository;
import com.group27.OnlyBuns.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testAddLike_NewLike() {
        Long postId = 1L;
        Long userId = 1L;

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());

        Like addedLike = postService.addLike(postId, userId);

        assertNotNull(addedLike);
        assertEquals(postId, addedLike.getPostId());
        assertEquals(userId, addedLike.getUserId());
        assertNotNull(addedLike.getCreatedAt());

        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    public void testAddLike_ExistingLike() {
        Long postId = 1L;
        Long userId = 1L;

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Like existingLike = new Like();
        existingLike.setPostId(postId);
        existingLike.setUserId(userId);
        when(likeRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.of(existingLike));

        Like addedLike = postService.addLike(postId, userId);

        assertNull(addedLike);

        verify(likeRepository, times(0)).save(any(Like.class));
    }
}
