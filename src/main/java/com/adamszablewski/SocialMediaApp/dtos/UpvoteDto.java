package com.adamszablewski.SocialMediaApp.dtos;

import com.adamszablewski.model.Comment;
import com.adamszablewski.model.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpvoteDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    @OneToOne
    private Post post;
    @OneToOne
    private Comment comment;
}
