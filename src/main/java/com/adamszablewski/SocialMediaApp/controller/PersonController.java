package com.adamszablewski.SocialMediaApp.controller;


import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.PersonDto;
import com.adamszablewski.SocialMediaApp.dtos.RegisterDto;
import com.adamszablewski.SocialMediaApp.exceptions.CustomExceptionHandler;
import com.adamszablewski.SocialMediaApp.service.PersonService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class PersonController {
    private final PersonService personService;

    @GetMapping("/{searchText}")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<List<PersonDto>> searchForUsers(@PathVariable String searchText){
        return ResponseEntity.ok(personService.searchForUsers(searchText));
    }

    @GetMapping()
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<PersonDto> getPersonById(@RequestParam(name = "userId")long userId){
        return ResponseEntity.ok(personService.getPersonDto(userId));
    }

    @GetMapping("/email")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<PersonDto> getPersonByEmail(@RequestParam(name = "email")String email){
        return ResponseEntity.ok(personService.getPersonDto(email));
    }
    @GetMapping("/convert")
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<Long> getUserIdForUsername(@RequestParam(name = "email")String email){
        return ResponseEntity.ok(personService.getUserIdByEmail(email));
    }
    @PostMapping()
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> createUser(@RequestBody RegisterDto registerDto){
        personService.createUser(registerDto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping()
    @SecureUserIdResource
    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "fallBackMethod")
    @RateLimiter(name = "rateLimiter")
    public ResponseEntity<String> deleteUser(@RequestParam long userId,
                                             HttpServletRequest servletRequest){
        personService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
    public  ResponseEntity<?> fallBackMethod(Throwable throwable){
        return CustomExceptionHandler.handleException(throwable);
    }

}
