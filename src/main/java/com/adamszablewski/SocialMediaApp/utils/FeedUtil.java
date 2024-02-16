package com.adamszablewski.SocialMediaApp.utils;


import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FeedUtil {

    private final CustomSortingUtil sortingUtil;
    private final PostRepository postRepository;
    public List<Long> getFriendsForUser(long userId){
        return friendServiceClient.getFriendsForUser(userId);
    }
    public List<Post> getPostsForUser(long userId){
        return postRepository.getAllByUserId(userId).stream()
                .filter(Post::isVisible)
                .toList();
    }
}
