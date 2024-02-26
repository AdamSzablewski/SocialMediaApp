package com.adamszablewski.SocialMediaApp.AOP;


//import com.adamszablewski.utils.security.SecurityUtil;
import com.adamszablewski.SocialMediaApp.exceptions.NotAuthorizedException;
import com.adamszablewski.SocialMediaApp.security.SecurityService;
import com.adamszablewski.SocialMediaApp.annotations.SecureResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
@AllArgsConstructor
public class SecureResourceAspect {

    private final SecurityService securityService;

    @Before("@annotation(com.adamszablewski.SocialMediaApp.annotations.SecureResource)")
    public void processSecureResource(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest request) {
                String token = request.getHeader("Authorization");

                if (token == null){
                    throw new NotAuthorizedException("Missing token exception");
                }
                boolean validated = securityService.validateToken(token);
                if (!validated){
                    throw new NotAuthorizedException();
                }
            }
        }
    }
}
