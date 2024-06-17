package com.adamszablewski.SocialMediaApp.AOP;


//import com.adamszablewski.utils.security.SecurityUtil;
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
public class SecureUserIdResourceAspect {

    private final SecurityService securityService;

    @Before("@annotation(com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource)")
    public void processSecureuserIdResource(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest request) {

                String token = request.getHeader("Authorization");
                String userId = request.getParameter("userId");
                boolean validated = false;
                if (token == null){
                    throw new NotAuthorizedException("Missing token exception");
                }
                if (userId != null && userId.length() > 0){
                    validated = securityService.isUser(Long.parseLong(userId), token);
                }

                if (!validated){
                    throw new NotAuthorizedException();
                }

            }
        }
    }
}
