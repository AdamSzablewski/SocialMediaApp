package com.adamszablewski.SocialMediaApp.controller;

import com.adamszablewski.SocialMediaApp.annotations.SecureContentResource;
import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.CommentDto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.exceptions.CustomExceptionHandler;
import com.adamszablewski.SocialMediaApp.service.CommentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;


    @PostMapping()
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> postCommentForPost(@RequestParam(name = "postId") long postId,
                                                     @RequestParam(name = "userId") long userId,
                                                     @RequestBody Comment comment,
                                                     HttpServletRequest servletRequest){
        commentService.postCommentForPost(postId, comment, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/comment")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> postCommentForComment(@RequestParam(name = "commentId") long commentId,
                                                         @RequestParam(name = "userId") long userId,
                                                         @RequestBody Comment comment,
                                                        HttpServletRequest servletRequest){
        commentService.postCommentForComment(commentId, comment, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping()
    @SecureContentResource(value = "commentId")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deleteCommentForPost(@RequestParam(name = "postId") long postId,
                                                        @RequestParam(name = "commentId") long commentId,
                                                       HttpServletRequest servletRequest){
        commentService.deleteCommentForPost(postId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping()
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<List<CommentDto>> getCommentsForResource(@RequestParam(name = "resourceId") long resourceId,
                                                               @RequestParam(name = "isComment") boolean isComment,
                                                               HttpServletRequest servletRequest){
        return  ResponseEntity.ok(commentService.getCommentsForResource(resourceId, isComment));
    }
    @DeleteMapping("/delete")
    @SecureContentResource(value = "commentId")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deleteComment(@RequestParam(name = "parentId") long parentId,
                                                @RequestParam(name = "commentId") long commentId,
                                                HttpServletRequest servletRequest){
        commentService.deleteCommentForComment(parentId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        return CustomExceptionHandler.handleException(throwable);
    }

}