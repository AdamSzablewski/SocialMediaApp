package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.dtos.TextPostDto;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.PostType;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.service.ImageService;
import com.adamszablewski.SocialMediaApp.service.PostService;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private UniqueIdGenerator uniqueIdGenerator;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void deletePostById_shouldDelete(){
        postService.deletePostById(1L);
        verify(postRepository).deleteById(1L);
    }
    @Test
    void createPost_shouldCreatePublicPost() {
        long userId = 1L;

        LocalDateTime currentTime = LocalDateTime.now();
        Person user = Person.builder()
                .id(userId)
                .build();

        when(personRepository.findById(userId)).thenReturn(Optional.of(user));
        Post post = postService.createPost(PostType.IMAGE, userId, true);

        assertThat(post.getPostType()).isEqualTo(PostType.IMAGE);
        assertThat(post.getUserId()).isEqualTo(userId);
        assertThat(post.getComments()).isEmpty();
        assertThat(post.getPerson()).isEqualTo(user);
        assertThat(post.getLikes()).isEmpty();
        assertThat(post.isVisible()).isTrue();
        assertThat(post.isPublic()).isTrue();
        assertThat(post.getCreationTime()).isAfterOrEqualTo(currentTime);
    }
    @Test
    void createPost_shouldCreatePrivatePost() {
        long userId = 1L;

        LocalDateTime currentTime = LocalDateTime.now();
        Person user = Person.builder()
                .id(userId)
                .build();

        when(personRepository.findById(userId)).thenReturn(Optional.of(user));
        Post post = postService.createPost(PostType.IMAGE, userId, false);

        assertThat(post.getPostType()).isEqualTo(PostType.IMAGE);
        assertThat(post.getUserId()).isEqualTo(userId);
        assertThat(post.getComments()).isEmpty();
        assertThat(post.getPerson()).isEqualTo(user);
        assertThat(post.getLikes()).isEmpty();
        assertThat(post.isVisible()).isTrue();
        assertThat(post.isPublic()).isFalse();
        assertThat(post.getCreationTime()).isAfterOrEqualTo(currentTime);
    }


    @Test
    void publishPost_shouldPublishPost() {
        String multimediaId = "imageId";
        TextPostDto postDto = TextPostDto.builder()
                .description("New Description")
                .build();
        Post post = new Post();
        when(postRepository.findByMultimediaId(multimediaId)).thenReturn(Optional.of(post));

        postService.publishPost(multimediaId, postDto);

        assertThat(post.getText()).isEqualTo("New Description");
        assertThat(post.isVisible()).isTrue();
        verify(postRepository).save(post);
    }

//    @Test
//    void uploadVideoForPost_shouldUploadVideoForPost(){
//        long userId = 1L;
//        String multimediaId = "IMAGEID";
//        MultipartFile video = mock(MultipartFile.class);
//        when(uniqueIdGenerator.generateUniqueImageId()).thenReturn(multimediaId);
//
//        postService.uploadVideoForPost(userId, video);
//
//        verify(imageService).upploadVideoToS3(video, multimediaId);
//        verify(postService).createHiddenPost(PostType.VIDEO, userId, multimediaId);
//    }


//    @Test
//    void createHiddenPost_shouldCreateHiddenPost() {
//        long userId = 1L;
//        String multimediaId = "MULTIMEDIAID";
//        Post post = postService.createPost(PostType.VIDEO, userId, false);
//        Person user = Person.builder()
//                .id(userId)
//                .build();
//        when(personRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        postService.createHiddenPost(PostType.VIDEO, userId, multimediaId);
//
//        verify(postRepository).save(eq(post));
//       /// verify(profileRepository).save(eq);
//
//    }

//    @Test
//    void createHiddenPost_shouldRollbackOnException() {
//        long userId = 1L;
//        String multimediaId = "someMultimediaId";
//        Post post = postService.createPost(PostType.VIDEO, userId);
//        post.setVisible(false);
//        Profile profile = new Profile();
//        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
//        doThrow(new RuntimeException()).when(postRepository).save(any());
//
//        assertThrows(RuntimeException.class, () -> postService.createHiddenPost(PostType.VIDEO, userId, multimediaId));
//
//        verify(kafkaMessagePublisher).sendDeletedVideoMessage(multimediaId);
//    }
}