package com.adamszablewski.SocialMediaApp.enteties;

import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne
    @JsonIgnoreProperties("messages")
    private Conversation conversation;
    private String text;
    @ManyToOne
    private Profile sender;
    private LocalDateTime dateTime;




}
