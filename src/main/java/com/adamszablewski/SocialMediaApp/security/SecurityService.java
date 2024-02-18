package com.adamszablewski.SocialMediaApp.security;



import com.adamszablewski.SocialMediaApp.dtos.LoginDto;
import com.adamszablewski.SocialMediaApp.enteties.users.Person;
import com.adamszablewski.SocialMediaApp.exceptions.InvalidCredentialsException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.utils.JwtUtil;
import com.adamszablewski.SocialMediaApp.utils.TokenGenerator;
import com.adamszablewski.SocialMediaApp.utils.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SecurityService {


    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final JwtUtil jwtUtil;
    private final UserValidator userValidator;
    private final TokenGenerator tokenGenerator;
    //private final OtpManager otpManager;
    public String validateUser(LoginDto loginDto) {
        Person user = getPerson(loginDto);
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException();
        }
        return generateTokenFromEmail(loginDto.getUsername());
    }
    private Person getPerson(LoginDto loginDto){
        return personRepository.findByEmail(loginDto.getUsername())
                .orElseThrow(NoSuchUserException::new);
    }
    private Person getPerson(String email){
        return personRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new);
    }


    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

//    public boolean validateOwner(long facilityId, String userMail) {
//       return userValidator.isOwner(facilityId, userMail);
//    }
//
//    public Boolean validateEmployee(long facilityId, String userMail) {
//        return userValidator.isEmployee(facilityId, userMail);
//    }
//
//    public Boolean validateUserEmail(long userId, String userMail) {
//        return userValidator.isUser(userId, userMail);
//    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public long extractUserIdFromToken(String token) {
        return jwtUtil.getUserIdFromToken(token);
    }

    public String generateTokenFromEmail(String email) {
        Person user = getPerson(email);
        return tokenGenerator.generateToken(user.getId());
    }
    public String generateToken(long userId){
        return tokenGenerator.generateToken(userId);
    }

//    public void sendOTP(String phoneNumber, long userId) {
//        String userPhoneNumber = userServiceClient.getPhoneNumberForUser(userId);
//        if (!userPhoneNumber.equals(phoneNumber)){
//            throw new NotAuthorizedException("Phone number does not match any account");
//        }
//        String oneTimePassword = otpManager.generateOTP();
//        Otp otp = Otp.builder()
//                .otp(oneTimePassword)
//                .dateTime(LocalDateTime.now())
//                .build();
//        OtpEvent event = OtpEvent.builder()
//                .otp(oneTimePassword)
//                .phoneNumer(phoneNumber)
//                .userId(userId)
//                .build();
//        otpRepository.save(otp);
//        kafkaMessagePublisher.sendOTPEventMessage(event);
//    }
//    public String validateOTP(String oneTimePassword, long userId){
//        Otp otp = otpRepository.findByOtp(oneTimePassword)
//                .orElseThrow(EntityNotFoundException::new);
//        boolean isValid =  otpManager.validateOTP(userId, otp);
//        if (isValid){
//            otpRepository.delete(otp);
//            return generateToken(userId);
//        }else {
//            throw new NotAuthorizedException("validation failed");
//        }
//    }

}
