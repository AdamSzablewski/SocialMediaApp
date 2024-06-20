package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.dtos.TextPostDto;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Video;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.PostType;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchPostException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.exceptions.WrongTypeException;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.s3.S3service;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final S3service s3service;
    private final ImageService imageService;
    private final PersonRepository personRepository;
    private final UniqueIdGenerator uniqueIdGenerator;
    public void deletePostById(long postId) {
        Post post = postRepository.findById(postId)
                        .orElseThrow(NoSuchPostException::new);
        Profile profile = post.getPerson().getProfile();
        profile.getPosts().remove(post);

        postRepository.deleteById(postId);
    }

    public void postTextPost(TextPostDto postDto, long userId, boolean isPublic) {
        Post post = createPost(PostType.TEXT, userId, isPublic);
        post.setText(postDto.getText());
        postRepository.save(post);
    }

    public Post createPost(PostType postType,  long userId, boolean isPublic){
        Person user = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Post post = Post.builder()
                .postType(postType)
                .person(user)
                .userId(userId)
                .isPublic(isPublic)
                .comments(new ArrayList<>())
                .likes(new HashSet<>())
                .visible(true)
                .creationTime(LocalDateTime.now())
                .build();
        postRepository.save(post);
        return post;
    }
    @Transactional
    public String uploadImageForPost(long userId, MultipartFile image) {
        String multimediaId = uniqueIdGenerator.generateUniqueImageId();
        imageService.upploadImageToS3(image, multimediaId);
        createHiddenPost(PostType.IMAGE, userId, multimediaId);
        return multimediaId;
    }
    @Transactional
    public void publishPost(String multimediaId, TextPostDto postDto) {
        Post post = postRepository.findByMultimediaId(multimediaId)
                .orElseThrow(NoSuchPostException::new);
        post.setText(postDto.getDescription());
        post.setCreationTime(LocalDateTime.now());
        post.setVisible(true);
        postRepository.save(post);
    }
    public String uploadVideoForPost(long userId, MultipartFile file) {
        Video video = imageService.createVideo(userId);
        imageService.upploadVideoToS3(file, video.getMultimediaId());
        createHiddenPost(PostType.VIDEO,userId, video.getMultimediaId());
        return video.getMultimediaId();

    }

    @Transactional
    public void createHiddenPost(PostType type, long userId, String multimediaID){

            Post post = createPost(type, userId, false);
            post.setVisible(false);
            post.setMultimediaId(multimediaID);
            Person person = personRepository.findById(userId)
                    .orElseThrow(NoSuchUserException::new);
            Profile profile = person.getProfile();
            try{
                if (profile.getPosts() == null){
                    profile.setPosts(new ArrayList<>());
                }
                profile.getPosts().add(post);
                postRepository.save(post);
                profileRepository.save(profile);
            } catch (Exception e){
                throw new RuntimeException();
            }
    }

    public List<PostDto> getAllPostByUser(long userId) {
        return postRepository.getAllByUserId(userId)
                .stream()
                .map(Mapper::mapPostToDto)
                .toList();
    }
}
