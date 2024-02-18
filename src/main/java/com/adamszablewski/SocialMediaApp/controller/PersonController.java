package com.adamszablewski.SocialMediaApp.controller;


import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.PersonDto;
import com.adamszablewski.SocialMediaApp.enteties.users.Person;
import com.adamszablewski.SocialMediaApp.service.users.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class PersonController {
    private final PersonService personService;

    @GetMapping()
    public ResponseEntity<PersonDto> getPersonById(@RequestParam(name = "userId")long userId){
        return ResponseEntity.ok(personService.getPerson(userId));
    }

    @GetMapping("/email")
    public ResponseEntity<PersonDto> getPersonByEmail(@RequestParam(name = "email")String email){
        return ResponseEntity.ok(personService.getPerson(email));
    }
    @GetMapping("/convert")
    public ResponseEntity<Long> getUserIdForUsername(@RequestParam(name = "email")String email){
        return ResponseEntity.ok(personService.getUserIdForUsername(email));
    }
    @PatchMapping("/reset-password")
    @SecureUserIdResource
    public ResponseEntity<String> resetUserPassword(@RequestParam("password") String password,
                                                    @RequestParam("userId") long userId){
        personService.resetPassword(password, userId);
        return ResponseEntity.ok().build();
    }
    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody Person person){
        personService.createUser(person);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/phoneNumber")
    public ResponseEntity<String> getPhoneNumber(@RequestParam long userId){
        return ResponseEntity.ok(personService.getPhoneNumber(userId));
    }
    @DeleteMapping()
    @SecureUserIdResource
    public ResponseEntity<String> deleteUser(@RequestParam long userId){
        personService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
