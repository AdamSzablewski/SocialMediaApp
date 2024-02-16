package com.adamszablewski.SocialMediaApp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
    Allows for the SecureUserIdAspect to validate user id from JWT token before executing controller method
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecureUserIdResource {
}
