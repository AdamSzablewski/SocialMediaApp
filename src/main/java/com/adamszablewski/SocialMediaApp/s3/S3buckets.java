package com.adamszablewski.SocialMediaApp.s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class S3buckets {
    @Value("${aws.s3.buckets.images}")
    private String imageBucket;
    @Value("${aws.s3.buckets.videos}")
    private String videoBucket;

}
