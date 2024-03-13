package com.adamszablewski.SocialMediaApp.security;



import com.adamszablewski.SocialMediaApp.dtos.LoginDto;
import com.adamszablewski.SocialMediaApp.enteties.*;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;
import com.adamszablewski.SocialMediaApp.exceptions.*;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.repository.OtpRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.CommentRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.LikeRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.service.EmailSenderService;
import com.adamszablewski.SocialMediaApp.utils.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


@AllArgsConstructor
@Service
public class SecurityService {


    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final TokenGenerator tokenGenerator;
    private final CommentRepository commentRepository;
    private final ConversationRepository conversationRepository;
    private final OtpRepository otpRepository;
    private final PersonManager personManager;
    private final OtpManager otpManager;
    private final EmailManager emailManager;
    private final EmailSenderService emailSenderService;

    public JWT validateUser(LoginDto loginDto) {
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

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public long extractUserIdFromToken(String token) {
        return jwtUtil.getUserIdFromToken(token);
    }

    public JWT generateTokenFromEmail(String email) {
        Person user = getPerson(email);
        return tokenGenerator.generateToken(user.getId());
    }
    public JWT generateToken(long userId){
        return tokenGenerator.generateToken(userId);
    }

    public boolean ownsComment(long commentId, String token) {
        long userId = extractUserIdFromToken(token);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NoSuchCommentException::new);
        return comment.getUser().getId() == userId;
    }

    public boolean ownsPost(long postId, String token) {
        long userId = extractUserIdFromToken(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);
        return post.getUserId() == userId;
    }

    public boolean ownsUpvote(long upvoteId, String token) {
        long userId = extractUserIdFromToken(token);
        Upvote upvote = likeRepository.findById(upvoteId)
                .orElseThrow(NoSuchUserException::new);
        return upvote.getUser().getId() == userId;

    }

    public boolean ownsMultimedia(long multimediaId, String token) {
        return true;
        //todo temp
    }

    public boolean isUser(long userId, String token) {
        return extractUserIdFromToken(token) == userId;
    }

    public boolean ownsConversation(long conversationId, String token) {
        long userId = extractUserIdFromToken(token);
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(NoSuchConversationFoundException::new);
        return conversation.getParticipants().stream()
                .anyMatch(participant -> participant.getId() == userId);
    }
    public void sendOTP(String email, long userId) {
        Person user = personManager.getPerson(email);
        if (user.getId() != userId){
            throw new NotAuthorizedException("Email does not match any account");
        }
        String oneTimePassword = otpManager.generateOTP();
        otpRepository.findByUserId(userId)
                .ifPresent(otpRepository::delete);
        Otp otp = Otp.builder()
                .userId(userId)
                .otp(oneTimePassword)
                .createdTime(LocalDateTime.now())
                .build();
        otpRepository.save(otp);
        Email emailMessage = emailManager.getOTPEmail(otp);
        CompletableFuture.runAsync(()-> emailSenderService.sendEmail(email, emailMessage));
    }
    public JWT validateOTP(String oneTimePassword, long userId){
        Otp otp = otpRepository.findByUserId(userId)
                .orElseThrow(EntityNotFoundException::new);

        if (otp.getOtp().equals(oneTimePassword)){
            otpRepository.delete(otp);
            return generateToken(userId);
        }else {
            throw new NotAuthorizedException("validation failed");
        }
    }

    public JWT getJWTByOTP(String password, long userId) {
        return validateOTP(password, userId);
    }
}
