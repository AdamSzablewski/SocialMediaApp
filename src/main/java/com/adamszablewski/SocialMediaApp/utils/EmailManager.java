package com.adamszablewski.SocialMediaApp.utils;

import com.adamszablewski.SocialMediaApp.enteties.Email;
import com.adamszablewski.SocialMediaApp.enteties.Otp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@AllArgsConstructor
public class EmailManager {


    public Email getOTPEmail(Otp otp){
        String text = String.format("Your one time password is **%s** it will expire on %s", otp.getOtp(),
                LocalTime.of(otp.getCreatedTime().getHour(), otp.getCreatedTime().getMinute()+5));
        return Email.builder()
                .textMessage(text)
                .subject("SocialApp One time password")
                .build();
    }
}
