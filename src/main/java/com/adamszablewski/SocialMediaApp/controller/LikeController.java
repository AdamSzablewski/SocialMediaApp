package com.adamszablewski.SocialMediaApp.controller;

import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.UpvoteDto;
import com.adamszablewski.SocialMediaApp.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/likes")
public class LikeController {


    private final LikeService likeService;

    @GetMapping("/like")
    public ResponseEntity<String> likePost(@RequestParam("postId") long postId,
                                             @RequestParam("userId")long userId,
                                             HttpServletRequest servletRequest){
        likeService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/unlike")
    @SecureUserIdResource
    public ResponseEntity<String> unLikePost(@RequestParam("postId") long postId,
                                            @RequestParam("userId")long userId,
                                             HttpServletRequest servletRequest){
        likeService.unLikePost(postId, userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/comments/like")
    @SecureUserIdResource
    public ResponseEntity<String> likeComment(@RequestParam("commentId") long commentId,
                                                @RequestParam("userId")long userId,
                                                HttpServletRequest servletRequest){
        likeService.likeComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/comments/unlike")
    @SecureUserIdResource
    public ResponseEntity<String> unLikeComment(@RequestParam("commentId") long commentId,
                                              @RequestParam("userId")long userId,
                                                HttpServletRequest servletRequest){
        likeService.unLikeComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comments")
    public ResponseEntity<List<UpvoteDto>> getLikesForComment(@RequestParam("commentId") long commentId){
        return ResponseEntity.ok(likeService.getLikesForComment(commentId));
    }

}
