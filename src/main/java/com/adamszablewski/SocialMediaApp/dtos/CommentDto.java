package com.adamszablewski.SocialMediaApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentDto {
    @Id
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String text;
    private long userId;
    private Set<UpvoteDto> likesDetailed;
    private int likes;
    private int comments;
    private List<CommentDto> answers;
}
