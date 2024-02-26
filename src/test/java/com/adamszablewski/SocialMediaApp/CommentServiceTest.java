package com.adamszablewski.SocialMediaApp;

import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.CommentRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yaml.snakeyaml.events.CommentEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommentServiceTest {
    @InjectMocks
    CommentService commentService;
    @Mock
    PostRepository postRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    PersonRepository personRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteCommentForPostTest_should_delete_comment(){
        long postId = 1L;
        long commentId = 1L;
        Comment comment = Comment.builder()
                .id(1L)
                .build();
        Comment comment2 = Comment.builder()
                .id(2L)
                .build();
        Post post = Post.builder()
                .id(1L)
                .comments(new ArrayList<>())
                .build();
        post.getComments().addAll(List.of(comment, comment2));

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        commentService.deleteCommentForPost(postId, commentId);

        assertThat(post.getComments().contains(comment)).isFalse();
        assertThat(post.getComments().contains(comment2)).isTrue();
    }

    @Test
    void postCommentForPostTest_0comments_shoud_create_comment() {
        long postId = 1L;
        long userId = 1L;
        Person person = Person.builder()
                .id(userId)
                .build();

        Comment commentData = Comment.builder()
                .text("New Comment")
                .user(person)
                .build();
        Post post = Post.builder()
                .id(postId)
                .comments(new ArrayList<>())
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(personRepository.findById(userId)).thenReturn(Optional.of(person));

        commentService.postCommentForPost(postId, commentData, userId);

        assertThat(post.getComments()).hasSize(1);
        Comment postedComment = post.getComments().get(0);
        assertThat(postedComment.getText()).isEqualTo("New Comment");
        assertThat(postedComment.getUser().getId()).isEqualTo(1L);
        verify(commentRepository).save(postedComment);
        verify(postRepository).save(post);
    }

    @Test
    void postCommentForPostTest_1comments_shoud_create_comment() {
        long postId = 1L;
        long userId = 1L;
        Person person = Person.builder()
                .id(userId)
                .build();

        Comment commentData = Comment.builder()
                .text("New Comment")
                .user(person)
                .build();
        Comment oldComment = Comment.builder()
                .text("Old Comment")
                .user(person)
                .build();
        List<Comment> comments = new ArrayList<>();
        comments.add(oldComment);
        Post post = Post.builder()
                .id(postId)
                .comments(comments)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(personRepository.findById(userId)).thenReturn(Optional.of(person));

        commentService.postCommentForPost(postId, commentData, userId);

        assertThat(post.getComments()).hasSize(2);
        Comment postedComment = post.getComments().get(1);
        assertThat(post.getComments().get(0).getText()).isEqualTo("Old Comment");
        assertThat(postedComment.getText()).isEqualTo("New Comment");
        assertThat(postedComment.getUser().getId()).isEqualTo(1L);
        verify(commentRepository).save(postedComment);
        verify(postRepository).save(post);
    }


}