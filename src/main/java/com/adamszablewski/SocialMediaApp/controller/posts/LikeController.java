package com.adamszablewski.SocialMediaApp.controller.posts;

import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.service.posts.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/posts/write/likes")
public class LikeController {


    private final LikeService likeService;


    @DeleteMapping("/posts")
    @SecureUserIdResource
    public ResponseEntity<String> unLikePost(@RequestParam("postId") long postId,
                                            @RequestParam("userId")long userId,
                                             HttpServletRequest servletRequest){
        likeService.unLikePost(postId, userId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/comments")
    @SecureUserIdResource
    public ResponseEntity<String> unLikeComment(@RequestParam("commentId") long commentId,
                                              @RequestParam("userId")long userId,
                                                HttpServletRequest servletRequest){
        likeService.unLikeComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

}
