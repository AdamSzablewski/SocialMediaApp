package com.adamszablewski.SocialMediaApp.service.multimedia;


import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.ProfilePhoto;
import com.adamszablewski.SocialMediaApp.repository.ImageRepositroy;
import com.adamszablewski.SocialMediaApp.repository.ProfilePhotoRepository;
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

    private final ImageRepositroy imageRepositroy;
    private final ProfilePhotoRepository profilePhotoRepository;

    public String upploadImageToS3(MultipartFile file, String multimediaId) {

        if(multimediaId.length() == 0){
            throw new RuntimeException("Wrong imageID");
        }
        try {
            s3service.putObject(s3buckets.getCustomer(),
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
    public String createPhotoResource(MultipartFile file, long userId, String multimediaId) {
        Image photo = createPhoto(userId, multimediaId);
        upploadImageToS3(file, multimediaId);
        photo.setMultimediaId(multimediaId);
        imageRepositroy.save(photo);
        return multimediaId;
    }

    public Image createPhoto(long userId){
        String multimediaId = uniqueIdGenerator.generateUniqueImageId();
        return Image.builder()
                .userId(userId)
                .multimediaId(multimediaId)
                .localDateTime(LocalDateTime.now())
                .build();
    }
    public Image createPhoto(long userId, String multimediaId){
        return Image.builder()
                .userId(userId)
                .multimediaId(multimediaId)
                .localDateTime(LocalDateTime.now())
                .build();
    }


    public void upploadVideoToS3(MultipartFile video, String multimediaId) {
    }

    public void updateProfilePhoto(MultipartFile photo, long userId) {
        Image image = createPhotoResource(photo, userId);
        ProfilePhoto profilePhoto = profilePhotoRepository.findByUserId(userId)
                .orElseGet(() -> createProfilePhoto(userId));
        if(profilePhoto.getImage() != null){
            Image imageToDelete = profilePhoto.getImage();
            profilePhoto.setImage(null);
            deleteImage(imageToDelete);
        }
        profilePhoto.setImage(image);
        profilePhotoRepository.save(profilePhoto);
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
