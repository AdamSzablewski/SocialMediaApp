package com.adamszablewski.SocialMediaApp.service;


import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.ProfilePhoto;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Video;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.ImageRepositroy;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.ProfilePhotoRepository;
import com.adamszablewski.SocialMediaApp.repository.VideoRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.s3.S3buckets;
import com.adamszablewski.SocialMediaApp.s3.S3service;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ImageService {

    private final S3service s3service;
    private final S3buckets s3buckets;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final ProfileRepository profileRepository;
    private final VideoRepository videoRepository;
    private final PersonRepository personRepository;

    private final ImageRepositroy imageRepositroy;
    private final ProfilePhotoRepository profilePhotoRepository;

    public String upploadImageToS3(MultipartFile file, String multimediaId) {

        if(multimediaId.length() == 0){
            throw new RuntimeException("Wrong imageID");
        }
        try {
            s3service.putObject(s3buckets.getImageBucket(),
                    multimediaId,
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return multimediaId;
    }

    @Transactional
    public void deleteUserData(Long userId) {
        //todo implement this
    }

    public Image createPhotoResource(MultipartFile file, long userId) {
        Image photo = createPhoto(userId);
        upploadImageToS3(file, photo.getMultimediaId());
        photo.setMultimediaId(photo.getMultimediaId());
        imageRepositroy.save(photo);
        return photo;
    }
    public Video createVideoResource(MultipartFile file, long userId) {
        Video video = createVideo(userId);
        upploadVideoToS3(file, video.getMultimediaId());
        video.setMultimediaId(video.getMultimediaId());
        videoRepository.save(video);
        return video;
    }
    public String createPhotoResource(MultipartFile file, long userId, String multimediaId) {
        Image photo = createPhoto(userId, multimediaId);
        upploadImageToS3(file, multimediaId);
        photo.setMultimediaId(multimediaId);
        imageRepositroy.save(photo);
        return multimediaId;
    }

    public Image createPhoto(long userId){
        String multimediaId = uniqueIdGenerator.generateUniqueImageId();
        Image image =  Image.builder()
                .userId(userId)
                .multimediaId(multimediaId)
                .localDateTime(LocalDateTime.now())
                .build();
        imageRepositroy.save(image);
        return image;
    }
    public Image createPhoto(long userId, String multimediaId){
        Image image =  Image.builder()
                .userId(userId)
                .multimediaId(multimediaId)
                .localDateTime(LocalDateTime.now())
                .build();
        imageRepositroy.save(image);
        return image;
    }


    public String upploadVideoToS3(MultipartFile file, String multimediaId) {
        if(multimediaId.length() == 0){
            throw new RuntimeException("Wrong imageID");
        }
        try {
            s3service.putObject(s3buckets.getVideoBucket(),
                    multimediaId,
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return multimediaId;
    }

    public Video createVideo(long userId){
        String multimediaId = uniqueIdGenerator.generateUniqueVideoId();
        Video video = Video.builder()
                .multimediaId(multimediaId)
                .userId(userId)
                .localDateTime(LocalDateTime.now())
                .build();
        videoRepository.save(video);
        return video;
    }
    @Transactional
    public void updateProfilePhoto(MultipartFile photo, long userId) {
        Image image = createPhotoResource(photo, userId);
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(NoSuchUserException::new);

        if(profile.getProfilePhoto() != null){
            Image imageToDelete = profile.getProfilePhoto();
            profile.setProfilePhoto(null);
            deleteImage(imageToDelete);
        }
        profile.setProfilePhoto(image);
        profileRepository.save(profile);

    }
    public ProfilePhoto createProfilePhoto(long userId){
        ProfilePhoto profilePhoto = ProfilePhoto.builder()
                .userId(userId)
                .build();
        profilePhotoRepository.save(profilePhoto);
        return profilePhoto;
    }
    private void deleteImage(Image image){
        imageRepositroy.delete(image);
    }
}
