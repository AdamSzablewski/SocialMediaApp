package com.adamszablewski.SocialMediaApp.controller;


import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.LoginDto;
import com.adamszablewski.SocialMediaApp.enteties.JWT;
import com.adamszablewski.SocialMediaApp.exceptions.CustomExceptionHandler;
import com.adamszablewski.SocialMediaApp.security.SecurityService;
import com.adamszablewski.SocialMediaApp.service.PersonService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final PersonService personService;
    private final SecurityService securityService;




    @PatchMapping("/reset-password")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> resetPassword(@RequestParam("password") String password,
                                                    @RequestParam("userId") long userId){
        personService.resetPassword(password, userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/login/otp")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> getJWTByOTP(@RequestParam("password") String password,
                                                @RequestParam("userId") long userId){
        securityService.getJWTByOTP(password, userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/otp")
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> getOtpForUser(@RequestParam("userId") long userId,
                                                @RequestParam("email") String email){
        personService.requestOtp(userId, email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<JWT> login(@RequestBody LoginDto user){

        securityService.validateUser(user);
        return ResponseEntity.ok(securityService.generateTokenFromEmail(user.getUsername()));
    }
    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        return CustomExceptionHandler.handleException(throwable);
    }

}
