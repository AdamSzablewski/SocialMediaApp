package com.adamszablewski.SocialMediaApp.controller;

import com.adamszablewski.SocialMediaApp.annotations.SecureContentResource;
import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.dtos.TextPostDto;
import com.adamszablewski.SocialMediaApp.exceptions.CustomExceptionHandler;
import com.adamszablewski.SocialMediaApp.service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/{userId}")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<List<PostDto>> getPostsByUser(HttpServletRequest servletRequest, @PathVariable long userId){

        return ResponseEntity.ok().body(postService.getAllPostByUser(userId));
    }

    @PostMapping()
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> postTextPost(HttpServletRequest servletRequest,
                                               @RequestBody TextPostDto postDto,
                                                @RequestParam(name = "userId") long userId,
                                               @RequestParam("isPublic") boolean isPublic){
        postService.postTextPost(postDto, userId, isPublic);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping(value = "/image/upload")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> uploadImageForPost(HttpServletRequest servletRequest,
                                                     @RequestParam(name = "userId") long userId,
                                                     @RequestParam MultipartFile image) {
        return ResponseEntity.ok().body(postService.uploadImageForPost(userId, image));
    }
    @PostMapping(value = "/video/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecureContentResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> uploadVideoForPost(HttpServletRequest servletRequest, @RequestParam(name = "userId") long userId,
                                           @RequestParam("video") MultipartFile video) throws IOException {

        return ResponseEntity.ok(postService.uploadVideoForPost(userId, video));
    }

    @PostMapping("/publish")
    @SecureContentResource("multimediaId")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> publishMultimediaPost(HttpServletRequest servletRequest,
                                                   @RequestParam(name = "multimediaId") String multimediaId,
                                                   @RequestBody TextPostDto postDto ) {
        postService.publishPost(multimediaId, postDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @SecureContentResource(value = "postId")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deletePost(HttpServletRequest servletRequest, @RequestParam(name = "postId") long postId){
        postService.deletePostById(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping()
    @SecureContentResource(value = "postId")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deletePostById(HttpServletRequest servletRequest, @RequestParam(name = "postId") long postId){
        postService.deletePostById(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        return CustomExceptionHandler.handleException(throwable);
    }

}
