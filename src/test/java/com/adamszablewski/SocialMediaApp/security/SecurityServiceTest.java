package com.adamszablewski.SocialMediaApp.security;

import com.adamszablewski.SocialMediaApp.dtos.LoginDto;
import com.adamszablewski.SocialMediaApp.enteties.JWT;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequest;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequestStatus;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.exceptions.InvalidCredentialsException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchCommentException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchConversationFoundException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchPostException;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.CommentRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.LikeRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.service.CommentService;
import com.adamszablewski.SocialMediaApp.service.FriendService;
import com.adamszablewski.SocialMediaApp.utils.JwtUtil;
import com.adamszablewski.SocialMediaApp.utils.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

public class SecurityServiceTest {
    @InjectMocks
    SecurityService securityService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PostRepository postRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ConversationRepository conversationRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void validateUserTest_shouldValidate(){
        Person person = Person.builder()
                .id(1L)
                .email("email")
                .password("password")
                .build();
        LoginDto loginDto = LoginDto.builder()
                .username(person.getEmail())
                .password(person.getPassword())
                .build();
        when(personRepository.findByEmail(loginDto.getUsername())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(loginDto.getPassword(), person.getPassword())).thenReturn(true);

        JWT result = securityService.validateUser(loginDto);
        assertThat(result != null);
    }
    @Test
    void validateUserTest_shouldThrowInvalidCredentialsException(){
        Person person = Person.builder()
                .id(1L)
                .email("email")
                .password("password")
                .build();
        LoginDto loginDto = LoginDto.builder()
                .username(person.getEmail())
                .password(person.getPassword())
                .build();
        when(personRepository.findByEmail(loginDto.getUsername())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(loginDto.getPassword(), person.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, ()->{
            securityService.validateUser(loginDto);
        });
    }
    @Test
    void validateTokenTest_shouldValidateToken(){
        securityService.validateToken("token");
        verify(jwtUtil).validateToken("token");
    }
    @Test
    void hashPasswordTest_shouldHash(){
        securityService.hashPassword("password");
        verify(passwordEncoder).encode("password");
    }
    @Test
    void extractUserIdFromTokenTest(){
        securityService.extractUserIdFromToken("token");
        verify(jwtUtil).getUserIdFromToken("token");
    }
    @Test
    void generateTokenFromEmailTest(){
        Person user = Person.builder()
                .id(1L)
                .email("email")
                .build();
        when(personRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        securityService.generateTokenFromEmail("email");
        verify(tokenGenerator).generateToken(user.getId());
    }
    @Test
    void generateTokenTest(){
        securityService.generateToken(1L);
        verify(tokenGenerator).generateToken(1L);
    }
    @Test
    void ownsCommentTest_shouldReturnTrue(){
        long commentId = 2L;
        Person user = Person.builder()
                .id(1L)
                .build();
        Comment comment = Comment.builder()
                .id(commentId)
                .user(user)
                .build();
        String token = "token";
        SecurityService spy = spy(securityService);
        when(spy.extractUserIdFromToken(token)).thenReturn(1L);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));


        boolean result = spy.ownsComment(commentId, token);

        assertTrue(result);
    }
    @Test
    void ownsCommentTest_shouldReturnFalse(){
        long commentId = 2L;
        Person user = Person.builder()
                .id(3L)
                .build();
        Comment comment = Comment.builder()
                .id(commentId)
                .user(user)
                .build();
        String token = "token";
        SecurityService spy = spy(securityService);
        when(spy.extractUserIdFromToken(token)).thenReturn(1L);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));


        boolean result = spy.ownsComment(commentId, token);

        assertFalse(result);
    }
    @Test
    void ownsCommentTest_shouldTHrowNoSuchCommentException(){
        long commentId = 2L;
        String token = "token";
        SecurityService spy = spy(securityService);
        when(spy.extractUserIdFromToken(token)).thenReturn(1L);
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(NoSuchCommentException.class, ()->{
            spy.ownsComment(commentId, token);
        });
    }
    @Test
    void ownsPostTest_shouldReturnTrue(){
        long postId = 2L;
        long userId = 1L;
        Post post = Post.builder()
                .id(postId)
                .userId(userId)
                .build();
        String token = "token";
        SecurityService spy = spy(securityService);
        when(spy.extractUserIdFromToken(token)).thenReturn(userId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));


        boolean result = spy.ownsPost(postId, token);

        assertTrue(result);
    }
    @Test
    void ownsPostTest_shouldReturnFalse(){
        long postId = 2L;
        long userId = 1L;
        Post post = Post.builder()
                .id(postId)
                .userId(userId)
                .build();
        String token = "token";
        SecurityService spy = spy(securityService);
        when(spy.extractUserIdFromToken(token)).thenReturn(3L);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));


        boolean result = spy.ownsPost(postId, token);

        assertFalse(result);
    }
    @Test
    void ownsPostTest_shouldTHrowNoSuchPostException(){
        long postId = 2L;
        String token = "token";
        SecurityService spy = spy(securityService);
        when(spy.extractUserIdFromToken(token)).thenReturn(1L);
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(NoSuchPostException.class, ()->{
            spy.ownsPost(postId, token);
        });
    }

}