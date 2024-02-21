package com.adamszablewski.SocialMediaApp.controller;

import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.service.multimedia.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
@RequestMapping("/images")
public class ImageController {


    private final ImageService imageService;

    @PostMapping("/profile")
    @SecureUserIdResource
    public ResponseEntity<String> likePost( @RequestParam MultipartFile image,
                                             @RequestParam("userId")long userId,
                                             HttpServletRequest servletRequest){
        imageService.updateProfilePhoto(image, userId);
        return ResponseEntity.ok().build();
    }

}
