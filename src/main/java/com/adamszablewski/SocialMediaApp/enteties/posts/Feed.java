package com.adamszablewski.SocialMediaApp.enteties.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Feed {

    private long userId;
    private List<Post> posts;

}
