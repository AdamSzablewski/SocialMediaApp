package com.adamszablewski.SocialMediaApp.enteties.posts;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Upvote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private LocalDateTime dateTime;
    @ManyToOne
    private Post post;
    @ManyToOne
    private Comment comment;
}
