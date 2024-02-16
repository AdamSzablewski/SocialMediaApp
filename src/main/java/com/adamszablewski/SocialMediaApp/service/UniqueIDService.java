package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UniqueIDService {

    private final UniqueIdGenerator uniqueIdGenerator;

    public String getUniqueVideoID() {
        return uniqueIdGenerator.generateUniqueVideoId();
    }
    public String getUniqueImageID() {
        return uniqueIdGenerator.generateUniqueImageId();
    }
}
