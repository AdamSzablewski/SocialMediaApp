package com.adamszablewski.SocialMediaApp.controller;


import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.LoginDto;
import com.adamszablewski.SocialMediaApp.enteties.JWT;
import com.adamszablewski.SocialMediaApp.security.SecurityService;
import com.adamszablewski.SocialMediaApp.service.users.PersonService;
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
    public ResponseEntity<String> resetUserPassword(@RequestParam("password") String password,
                                                    @RequestParam("userId") long userId){
        personService.resetPassword(password, userId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<JWT> login(@RequestBody LoginDto user){

        securityService.validateUser(user);
        return ResponseEntity.ok(securityService.generateTokenFromEmail(user.getUsername()));
    }

}
