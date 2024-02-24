package com.adamszablewski.SocialMediaApp.controller;


import com.adamszablewski.SocialMediaApp.annotations.SecureContentResource;
import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.ConversationDTO;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
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
    @SecureUserIdResource
    @CircuitBreaker(name = "messagingServiceCircuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "messagingServiceRateLimiter")
    public ResponseEntity<ConversationDTO> getCoversationBetweenUsers(@RequestParam("user1Id") long user1Id,
                                                                      @RequestParam("user2Id") long user2Id,
                                                                      HttpServletRequest httpServletRequest){


        return ResponseEntity.ok(conversationService.getCoversationsBetweenUsers(user1Id, user2Id));
    }
    @GetMapping("/user")
    @SecureUserIdResource
    @CircuitBreaker(name = "messagingServiceCircuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "messagingServiceRateLimiter")
    public ResponseEntity<List<ConversationDTO>> getAllConversationsForUser(@RequestParam("userId") long userId,
                                                                      HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(conversationService.getAllConversationsForUser(userId));
    }

    @DeleteMapping()
    @SecureContentResource
    @CircuitBreaker(name = "messagingServiceCircuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "messagingServiceRateLimiter")
    public ResponseEntity<String> deleteConversation(@RequestParam("conversationId") long conversationId,
                                                     HttpServletRequest httpServletRequest){

        conversationService.deleteConversation(conversationId);
        return ResponseEntity.ok().build();
    }
    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        return CustomExceptionHandler.handleException(throwable);
    }
   
}
