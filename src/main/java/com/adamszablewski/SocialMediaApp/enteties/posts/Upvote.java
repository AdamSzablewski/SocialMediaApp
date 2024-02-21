package com.adamszablewski.SocialMediaApp.enteties.posts;
import com.adamszablewski.SocialMediaApp.enteties.Person;
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
    @ManyToOne
    private Person user;
    private LocalDateTime dateTime;
    @ManyToOne
    private Post post;
    @ManyToOne
    private Comment comment;
}
