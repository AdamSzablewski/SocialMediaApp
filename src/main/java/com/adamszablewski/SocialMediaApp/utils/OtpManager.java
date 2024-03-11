package com.adamszablewski.SocialMediaApp.utils;

import com.adamszablewski.SocialMediaApp.enteties.Otp;
import com.adamszablewski.SocialMediaApp.exceptions.NotAuthorizedException;
import com.adamszablewski.SocialMediaApp.repository.OtpRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Component
@AllArgsConstructor
public class OtpManager {

    private final OtpRepository otpRepository;

    public boolean validateOTP(long userId, Otp otp){
        if (userId != otp.getUserId()){
            throw new NotAuthorizedException("Wrong user");
        }
        return checkExceedsTime(otp);
    }
    public boolean checkExceedsTime(Otp otp){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime previousTime = otp.getDateTime();
        long minutesDifference = ChronoUnit.MINUTES.between(currentTime, previousTime);
        if (minutesDifference > Otp.OTP_TIME_MAX){
            otpRepository.delete(otp);
            throw new NotAuthorizedException("time limit exceeded");
        }
        return true;
    }

    public String generateOTP(){
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }

}
