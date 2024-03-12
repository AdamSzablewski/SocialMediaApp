package com.adamszablewski.SocialMediaApp.controller;

import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.FriendListDto;
import com.adamszablewski.SocialMediaApp.dtos.FriendRequestDto;
import com.adamszablewski.SocialMediaApp.dtos.ProfileDto;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequest;
import com.adamszablewski.SocialMediaApp.exceptions.CustomExceptionHandler;
import com.adamszablewski.SocialMediaApp.service.FriendService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@Controller
@AllArgsConstructor
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;
    @GetMapping("/requests")
    @SecureUserIdResource
    @CircuitBreaker(name = "friendServiceCircuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "friendServiceRateLimiter")
    public ResponseEntity<List<FriendRequestDto>> getFriendRequestsForUser(@RequestParam(name = "userId") long userId,
                                                                           HttpServletRequest servletRequest){
        return ResponseEntity.ok(friendService.getFriendRequestsForUser(userId));
    }
    @GetMapping()
    @SecureUserIdResource
    @CircuitBreaker(name = "friendServiceCircuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "friendServiceRateLimiter")
    public ResponseEntity<FriendListDto> getFriendIdsForUser(@RequestParam(name = "userId") long userId,
                                                             HttpServletRequest servletRequest){
        return ResponseEntity.ok(friendService.getFriendsForUserId(userId));
    }
    @GetMapping("/add")
    @SecureUserIdResource
    @CircuitBreaker(name = "friendServiceCircuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "friendServiceRateLimiter")
    public ResponseEntity<String> sendFriendRequest(@RequestParam(name = "userId") long userId,
                                                    @RequestParam(name = "friendId") long friendId,
                                                    HttpServletRequest servletRequest){
        friendService.sendFriendRequest(userId, friendId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/answer")
    @SecureUserIdResource
    @CircuitBreaker(name = "friendServiceCircuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "friendServiceRateLimiter")
    public ResponseEntity<String> respondToFriendRequest(@RequestParam(name = "friendRequestId") long friendRequestId,
                                                         @RequestParam(name = "status")boolean status,
                                                         @RequestParam(name="userId") long userId,
                                                         HttpServletRequest servletRequest){
        friendService.respondToFriendRequest(friendRequestId, status);
        return ResponseEntity.ok().build() ;
    }
    @GetMapping("/remove")
    @SecureUserIdResource
    @CircuitBreaker(name = "friendServiceCircuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "friendServiceRateLimiter")
    public ResponseEntity<String> removeFriend(@RequestParam(name = "userId") long userId,
                                               @RequestParam(name = "friendId") long friendId,
                                               HttpServletRequest servletRequest){
        friendService.removeFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        return CustomExceptionHandler.handleException(throwable);
    }
}
