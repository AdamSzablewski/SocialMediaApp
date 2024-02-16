package com.adamszablewski.SocialMediaApp.service.posts;

import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.PostType;
import com.adamszablewski.SocialMediaApp.enteties.posts.Profile;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchPostException;
import com.adamszablewski.SocialMediaApp.exceptions.WrongTypeException;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import lombok.AllArgsConstructor;
import org.hibernate.event.spi.EventType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    public void deletePostById(long postId) {
        postRepository.deleteById(postId);
    }

    public void postTextPost(PostDto postDto, long userId) {
        Post post = createPost(PostType.TEXT, userId);
        fillTextPost(post, postDto);
        postRepository.save(post);
    }
    public Post fillTextPost(Post post, PostDto postDto){
        if (post.getPostType() != PostType.TEXT){
            throw new WrongTypeException();
        }
        post.setText(postDto.getText());
        return post;
    }
    public Post createPost(PostType postType,  long userId){
        return Post.builder()
                .postType(postType)
                .userId(userId)
                .comments(new ArrayList<>())
                .likes(new HashSet<>())
                .visible(true)
                .creationTime(LocalDateTime.now())
                .build();
    }
    @Transactional
    @Async
    public String uploadImageForPost(long userId, MultipartFile image) {
        String multimediaId = uniqueIDServiceClient.getUniqueImageId();
        CompletableFuture.runAsync(()-> imageServiceClient.sendImageToImageService(image, userId, multimediaId));
        createHiddenPost(PostType.IMAGE, userId, multimediaId);
        return multimediaId;
    }
    private Profile createProfile(long userId){
        Profile newProfile = Profile.builder()
                .userId(userId)
                .posts(new ArrayList<>())
                .build();
        profileRepository.save(newProfile);
        return newProfile;
    }
    public void publishPost(String multimediaId, PostDto postDto) {
        Post post = postRepository.findByMultimediaId(multimediaId)
                .orElseThrow(NoSuchPostException::new);
        post.setDescription(postDto.getDescription());
        post.setCreationTime(LocalDateTime.now());
        post.setVisible(true);
        postRepository.save(post);
    }
    public String uploadVideoForPost(long userId, MultipartFile video) {
        String multimediaId = uniqueIDServiceClient.getUniqueVideoId();
        videoServiceClient.sendVideoToVideoService(video, userId, multimediaId);
        createHiddenPost(PostType.VIDEO,userId, multimediaId);
        return multimediaId;

    }
    @Async
    @Transactional
    public void createHiddenPost(PostType type, long userId, String multimediaID){

            Post post = createPost(type, userId);
            post.setVisible(false);
            Profile profile = profileRepository.findByUserId(userId)
                    .orElseGet(()-> createProfile(userId));
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
