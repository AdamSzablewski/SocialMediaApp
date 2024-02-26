package com.adamszablewski.SocialMediaApp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
    Allows for the SecureReturnedResourceAspect aspect to validate user credentials before returning response to the client
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecureReturnedContentResource {
    String value() default "";
}
