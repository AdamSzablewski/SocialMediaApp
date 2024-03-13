package com.adamszablewski.SocialMediaApp.controller;

import com.adamszablewski.SocialMediaApp.service.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class EmailController {

    private EmailSenderService emailSenderService;



//    @GetMapping("/send")
//    public void send(){
//        emailSenderService.sendEmail();
//    }
}
