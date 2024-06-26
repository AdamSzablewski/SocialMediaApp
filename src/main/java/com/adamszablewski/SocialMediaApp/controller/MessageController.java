package com.adamszablewski.SocialMediaApp.controller;


import com.adamszablewski.SocialMediaApp.annotations.SecureContentResource;
import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.message.MessageCreateDto;
import com.adamszablewski.SocialMediaApp.dtos.message.MessageDTO;
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

@Controller
@AllArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    MessageService messageService;

    @PostMapping()
    @SecureContentResource("conversationId")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<MessageDTO> sendTextMessageToConversation(
                                                            @RequestParam("conversationId") long conversationId,
                                                            @RequestParam("userId") long userId,
                                                            @RequestBody MessageCreateDto message,
                                                            HttpServletRequest httpServletRequest) throws Exception {
        messageService.addTextMessageToConversation(userId, conversationId, message);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/image")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<MessageDTO> sendImageToConversation(@RequestParam("userId") long userId,
                                                              @RequestParam("conversationId")long conversationId,
                                                              @RequestParam MultipartFile image,
                                                              HttpServletRequest httpServletRequest) {
        messageService.addImageMessageToConversation(userId, conversationId, image);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/video")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<MessageDTO> sendVideoToConversation(@RequestParam("userId") long userId,
                                                              @RequestParam("conversationId")long conversationId,
                                                              @RequestParam MultipartFile video,
                                                              HttpServletRequest httpServletRequest) {
        messageService.addVideoMessageToConversation(userId, conversationId, video);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping()
    //@SecureContentResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deleteMessageFromConversation(@RequestParam("conversationId") long conversationId,
                                                                @RequestParam("messageId") long messageId,
                                                                @RequestParam("userId") long userId,
                                                                HttpServletRequest httpServletRequest){
        messageService.deleteMessage(conversationId, messageId);
        return ResponseEntity.ok().build();
    }


    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        throwable.printStackTrace();
        return CustomExceptionHandler.handleException(throwable);
    }
}
