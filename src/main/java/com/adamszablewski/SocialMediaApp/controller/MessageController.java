package com.adamszablewski.SocialMediaApp.controller;


import com.adamszablewski.SocialMediaApp.annotations.SecureContentResource;
import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.MessageDTO;
import com.adamszablewski.SocialMediaApp.enteties.Message;
import com.adamszablewski.SocialMediaApp.exceptions.CustomExceptionHandler;
import com.adamszablewski.SocialMediaApp.service.MessageService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    MessageService messageService;

    @PostMapping()
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<MessageDTO> sendMessageToUserById(@RequestParam("conversationId") long conversationId,
                                                            @RequestParam("userId") long userId,
                                                            @RequestBody Message message,
                                                            HttpServletRequest httpServletRequest){
        messageService.sendMessageToConversation(conversationId, userId, message);
        return ResponseEntity.ok().build();
    }
//    @PostMapping()
//    @SecureUserIdResource
//    public ResponseEntity<MessageDTO> sendMessageToConversationById(@PathVariable long recipientId,
//                                                            @RequestParam("userId") long userId,
//                                                            @RequestBody Message message,
//                                                            HttpServletRequest httpServletRequest){
//        messageService.sendMessageToUserById(recipientId, userId, message);
//        return ResponseEntity.ok().build();
//    }
    @PostMapping("/user/{recipientId}/from/{senderId}/image")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<MessageDTO> sendImageToUserById(@PathVariable long recipientId,
                                                                           @RequestParam("userId") long userId,
                                                                           @RequestParam MultipartFile image,
                                                                           HttpServletRequest httpServletRequest) throws IOException {
        messageService.sendImageToUserById(recipientId, userId, image);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{conversationId}/message/{messageId}/user/{ownerId}")
    @SecureContentResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deleteMessageFromConversationForUser(@RequestParam("conversationId") long conversationId,
                                                                       @RequestParam("messageId") long messageId,
                                                                       @RequestParam("userId") long ownerId,
                                                                       HttpServletRequest httpServletRequest){
        messageService.deleteMessageFromConversationForUser(conversationId, messageId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/message/instance")
    @SecureContentResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deleteMessageFromConversationForAll(@RequestParam("instanceId") String instanceId,
                                                                                           @RequestParam("userId") long ownerId,
                                                                                           HttpServletRequest httpServletRequest){
        messageService.deleteMessageFromConversationForAll(instanceId, ownerId);
        return ResponseEntity.ok().build();
    }

    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        return CustomExceptionHandler.handleException(throwable);
    }
}
