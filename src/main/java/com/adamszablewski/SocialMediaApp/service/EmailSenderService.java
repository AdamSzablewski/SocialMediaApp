package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.enteties.Email;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private  String senderEmail;
    @Async
    public void sendEmail(String receiverEmail, Email email){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(senderEmail);
        mail.setTo(receiverEmail);
        mail.setText(email.getTextMessage());
        mail.setSubject(email.getSubject());

        mailSender.send(mail);
    }
}
