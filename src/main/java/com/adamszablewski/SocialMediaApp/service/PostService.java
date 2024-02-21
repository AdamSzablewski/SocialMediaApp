package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.TextPostDto;
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
import com.adamszablewski.SocialMediaApp.service.multimedia.ImageService;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

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
        postRepository.deleteById(postId);
    }

    public void postTextPost(TextPostDto postDto, long userId, boolean isPublic) {
        Post post = createPost(PostType.TEXT, userId, isPublic);
        fillTextPost(post, postDto);
        postRepository.save(post);
    }
    public Post fillTextPost(Post post, TextPostDto postDto){
        if (post.getPostType() != PostType.TEXT){
            throw new WrongTypeException();
        }
        post.setText(postDto.getText());
        return post;
    }
    public Post createPost(PostType postType,  long userId, boolean isPublic){
        return Post.builder()
                .postType(postType)
                .userId(userId)
                .isPublic(isPublic)
                .comments(new ArrayList<>())
                .likes(new HashSet<>())
                .visible(true)
                .creationTime(LocalDateTime.now())
                .build();
    }
    @Transactional
    public String uploadImageForPost(long userId, MultipartFile image) {
        String multimediaId = uniqueIdGenerator.generateUniqueImageId();
       // imageService.upploadImageToS3(image, multimediaId);
        createHiddenPost(PostType.IMAGE, userId, multimediaId);
        System.out.println(multimediaId);
        return multimediaId;
    }
    private Profile createProfile(long userId){
        Profile newProfile = Profile.builder()
                .posts(new ArrayList<>())
                .build();
        profileRepository.save(newProfile);
        return newProfile;
    }
    public void publishPost(String multimediaId, TextPostDto postDto) {
        Post post = postRepository.findByMultimediaId(multimediaId)
                .orElseThrow(NoSuchPostException::new);
        post.setDescription(postDto.getDescription());
        post.setCreationTime(LocalDateTime.now());
        post.setVisible(true);
        postRepository.save(post);
    }
    public String uploadVideoForPost(long userId, MultipartFile video) {
        String multimediaId = uniqueIdGenerator.generateUniqueVideoId();
        imageService.upploadVideoToS3(video, multimediaId);
        createHiddenPost(PostType.VIDEO,userId, multimediaId);
        return multimediaId;

    }
    @Async
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
                if (type == PostType.VIDEO){
                  //  kafkaMessagePublisher.sendDeletedVideoMessage(multimediaID);
                }else {
                  //  kafkaMessagePublisher.sendDeleteImageMessage(multimediaID);
                }

                throw new RuntimeException();
            }

    }

}
