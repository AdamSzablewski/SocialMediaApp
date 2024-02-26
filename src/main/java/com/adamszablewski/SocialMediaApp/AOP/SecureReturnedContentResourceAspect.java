package com.adamszablewski.SocialMediaApp.AOP;


import com.adamszablewski.SocialMediaApp.annotations.SecureContentResource;
import com.adamszablewski.SocialMediaApp.annotations.SecureReturnedContentResource;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.exceptions.NotAuthorizedException;
import com.adamszablewski.SocialMediaApp.security.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
@AllArgsConstructor
public class SecureReturnedContentResourceAspect {

//    private final SecurityService securityService;
//
//    @AfterReturning("@annotation(secureReturnedContentResource) && args(.., request)")
//    public void processSecureContentResource(JoinPoint joinPoint, SecureReturnedContentResource secureContentResource,
//                                             Object returnedContent) {
//
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        String token = request.getHeader("token");
//
//        if (token == null) {
//            throw new NotAuthorizedException("Missing token exception");
//        }
//
//        switch (secureContentResource.value()) {
//            case "comment" -> {
//                long commentId = ((Comment) returnedContent).getId();
//                if (!securityService.ownsComment(commentId, token)) {
//                    throw new NotAuthorizedException();
//                    }
//                }
//
//
//            case "post" -> {
//                long postId = ((Post) returnedContent).getId(); // Modify this based on your return type
//                if (!securityService.ownsPost(postId, token)) {
//                    throw new NotAuthorizedException();
//                }
//
//            default:
//                throw new NotAuthorizedException();
//                }
//
//            }
//        }
//    }
}
