package com.adamszablewski.SocialMediaApp.controller;


import com.adamszablewski.SocialMediaApp.annotations.SecureContentResource;
import com.adamszablewski.SocialMediaApp.annotations.SecureResource;
import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.ConversationDTO;
import com.adamszablewski.SocialMediaApp.exceptions.CustomExceptionHandler;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.service.ConversationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final ConversationRepository conversationRepository;

    @GetMapping()
    @SecureResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<ConversationDTO> getCoversationBetweenUsers(@RequestParam("user1Id") long user1Id,
                                                                      @RequestParam("user2Id") long user2Id,
                                                                      HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(conversationService.getCoversationsBetweenUsers(user1Id, user2Id));
    }
    @GetMapping("/id")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<ConversationDTO> getCoversationById(@RequestParam("conversationId") long conversationId,
                                                              @RequestParam("userId") long userId,
                                                              HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(conversationService.getCoversationById(conversationId));
    }
    @GetMapping("/user")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<List<ConversationDTO>> getAllConversationsForUser(@RequestParam("userId") long userId,
                                                                      HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(conversationService.getConversationsForUser(userId));
    }

    @DeleteMapping()
    @SecureContentResource("conversationId")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deleteConversation(@RequestParam("conversationId") long conversationId,
                                                     HttpServletRequest httpServletRequest){

        conversationService.deleteConversation(conversationId);
        return ResponseEntity.ok().build();
    }
    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        return CustomExceptionHandler.handleException(throwable);
    }
   
}
