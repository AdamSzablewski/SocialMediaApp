package com.adamszablewski.SocialMediaApp.AOP;


import com.adamszablewski.SocialMediaApp.annotations.SecureContentResource;
import com.adamszablewski.SocialMediaApp.exceptions.NotAuthorizedException;
import com.adamszablewski.SocialMediaApp.security.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
@AllArgsConstructor
public class SecureContentResourceAspect {

    private final SecurityService securityService;

//    @Before("@annotation(secureContentResource) && args(.., request)")
    public void processSecureContentResource(JoinPoint joinPoint, SecureContentResource secureContentResource, HttpServletRequest request) {

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                String token = request.getHeader("Authorization");
                String postId = request.getParameter("postId");
                String upvoteId =request.getParameter("upvoteId");
                String commentId = request.getParameter("commentId");
                String multimediaId = request.getParameter("multimediaId");
                String conversationId = request.getParameter("conversationId");

                boolean validated;

                if (token == null){
                    throw new RuntimeException("Missing token exception");
                }

                switch (secureContentResource.value()){
                    case "commentId" -> {
                        assert commentId != null;
                        validated = securityService.ownsComment(Long.parseLong(commentId), token);
                    }
                    case "postId" -> {
                        assert postId != null;
                        validated = securityService.ownsPost(Long.parseLong(postId), token);
                    }
                    case "upvoteId" -> {
                        assert upvoteId != null;
                        validated = securityService.ownsUpvote(Long.parseLong(upvoteId), token);
                    }
                    case "multimediaId" -> {
                        assert commentId != null;
                        validated = securityService.ownsMultimedia(Long.parseLong(multimediaId), token);
                    }
                    case "conversationId" -> {
                        assert conversationId != null;
                        validated = securityService.ownsConversation(Long.parseLong(conversationId), token);
                    }
                    default -> throw new NotAuthorizedException();

                }

            }
        }
    }
}
