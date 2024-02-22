package com.adamszablewski.SocialMediaApp.enteties.friends;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Profile sender;
    @ManyToOne
    private Profile receiver;
    private LocalDateTime dateTime;
    private FriendRequestStatus status;
}
