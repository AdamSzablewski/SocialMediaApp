package com.adamszablewski.SocialMediaApp.dtos;

import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Message;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    private long id;
    private Set<Long> recievers;
    private ProfileDto sender;
    private LocalDateTime dateTime;
    private String message;

}
