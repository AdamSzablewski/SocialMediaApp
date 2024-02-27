package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;
import com.adamszablewski.SocialMediaApp.repository.posts.CommentRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.LikeRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void checkIfUserAlreadyLikedTesT_shouldReturnTrue_forComment(){

        long userId = 1L;
        Person user1 = Person.builder()
                .id(userId)
                .build();
        Person user2 = Person.builder()
                .id(2L)
                .build();
        Upvote upvote1 = Upvote.builder()
                .user(user1)
                .build();
        Upvote upvote2 = Upvote.builder()
                .user(user2)
                .build();
        Comment comment = Comment.builder()
                .likes(Set.of(upvote1, upvote2))
                .build();
        boolean result = likeService.checkIfUserAlreadyLiked(comment, userId);
        assertThat(result).isTrue();
    }
    @Test
    void checkIfUserAlreadyLikedTesT_shouldReturnFalse_forComment(){

        long userId = 1L;
        Person user1 = Person.builder()
                .id(2L)
                .build();
        Upvote upvote = Upvote.builder()
                .user(user1)
                .build();

        Comment comment = Comment.builder()
                .likes(Set.of(upvote))
                .build();
        boolean result = likeService.checkIfUserAlreadyLiked(comment, userId);
        assertThat(result).isFalse();
    }
    @Test
    void checkIfUserAlreadyLikedTesT_shouldReturnTrue_forPost(){

        long userId = 1L;
        Person user1 = Person.builder()
                .id(userId)
                .build();
        Person user2 = Person.builder()
                .id(2L)
                .build();
        Upvote upvote1 = Upvote.builder()
                .user(user1)
                .build();
        Upvote upvote2 = Upvote.builder()
                .user(user2)
                .build();
        Post post = Post.builder()
                .likes(Set.of(upvote1, upvote2))
                .build();
        boolean result = likeService.checkIfUserAlreadyLiked(post, userId);
        assertThat(result).isTrue();
    }
    @Test
    void checkIfUserAlreadyLikedTesT_shouldReturnFalse_forPost(){

        long userId = 1L;
        Person user1 = Person.builder()
                .id(2L)
                .build();
        Upvote upvote = Upvote.builder()
                .user(user1)
                .build();

        Post post = Post.builder()
                .likes(Set.of(upvote))
                .build();
        boolean result = likeService.checkIfUserAlreadyLiked(post, userId);
        assertThat(result).isFalse();
    }
    @Test
    void removeLikeTest_shouldRemoveFromPost(){
        long userId = 1L;
        Person user1 = Person.builder()
                .id(userId)
                .build();
        Person user2 = Person.builder()
                .id(2L)
                .build();
        Upvote upvote1 = Upvote.builder()
                .user(user1)
                .build();
        Upvote upvote2 = Upvote.builder()
                .user(user2)
                .build();
        Post post = Post.builder()
                .likes(new HashSet<>())
                .build();
        post.getLikes().add(upvote1);
        post.getLikes().add(upvote2);
        likeService.removeLike(post, userId);
        assertThat(post.getLikes().size()).isEqualTo(1);
        assertThat(post.getLikes().contains(upvote1)).isFalse();
    }
    @Test
    void removeLikeTest_shouldNotRemoveFromPost(){
        long userId = 1L;
        Post post = Post.builder()
                .likes(new HashSet<>())
                .build();
        likeService.removeLike(post, userId);
        assertThat(post.getLikes().size()).isEqualTo(0);
    }
    @Test
    void removeLikeTest_shouldRemoveFromComment(){
        long userId = 1L;
        Person user1 = Person.builder()
                .id(userId)
                .build();
        Person user2 = Person.builder()
                .id(2L)
                .build();
        Upvote upvote1 = Upvote.builder()
                .user(user1)
                .build();
        Upvote upvote2 = Upvote.builder()
                .user(user2)
                .build();
        Comment comment = Comment.builder()
                .likes(new HashSet<>())
                .build();
        comment.getLikes().add(upvote1);
        comment.getLikes().add(upvote2);
        likeService.removeLike(comment, userId);
        assertThat(comment.getLikes().size()).isEqualTo(1);
        assertThat(comment.getLikes().contains(upvote1)).isFalse();
    }
    @Test
    void removeLikeTest_shouldNotRemoveFromComment(){
        long userId = 1L;
        Comment comment = Comment.builder()
                .likes(new HashSet<>())
                .build();
        likeService.removeLike(comment, userId);
        assertThat(comment.getLikes().size()).isEqualTo(0);
    }

}
