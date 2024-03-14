package com.adamszablewski.SocialMediaApp.utils;


import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FeedUtil {

    private final CustomSortingUtil sortingUtil;
    private final PostRepository postRepository;
    public List<Long> getFriendsForUser(long userId){
        return new ArrayList<>();
        //todo implement get friends for user
    }
    public List<Post> getPostsForUser(long userId){
        return postRepository.getAllByUserId(userId).stream()
                .filter(Post::isVisible)
                .toList();
    }

    public List<Post> getPublicPosts() {
        return postRepository.findAllPublicAndVisiblePosts();
    }

    public Post addViewToPost(Post post) {
        postRepository.incrementViewCount(post.getId());
        return post;
    }
}
