package com.adamszablewski.SocialMediaApp.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UniqueIdGenerator {
    public String generateUniqueId() {

        UUID uuid = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();
        return uuid.toString() + "-" + timestamp;
    }


    public String generateUniqueImageId() {
        UUID uuid = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();
        return "IMAGE"+uuid+ "-" + timestamp;
    }
    public String generateUniqueVideoId() {
        UUID uuid = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();
        return "VIDEO"+uuid+ "-" + timestamp;
    }
}