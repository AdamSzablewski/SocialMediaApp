package com.adamszablewski.SocialMediaApp.service;


import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.utils.FeedUtil;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.adamszablewski.SocialMediaApp.utils.CustomSortingUtil.COMPARE_DAY_LIKE_COMMENT;

@Service
@AllArgsConstructor
public class FeedService {
    private final static int MAX_FEED_SIZE = 100;
    private final FeedUtil feedUtil;
    /**
     * Retrieves a list of PostDto objects for the specified user, including their own posts,
     * posts from friends, and additional public posts if needed to fill the feed to the maximum size of  {@value MAX_FEED_SIZE} .
     *
     * @param userId the ID of the user for whom to retrieve the feed
     * @return a list of PostDto objects representing the user's feed
     */
    public List<PostDto> getFeedForUser(long userId) {
        Stream<Post> userPosts = feedUtil.getPostsForUser(userId)
                .stream();
        Stream<Post> friendPosts = feedUtil.getFriendsForUser(userId)
                .stream()
                .map(feedUtil::getPostsForUser)
                .flatMap(List::stream);
        Set<Long> postIds = new HashSet<>();
        List<PostDto> feed = Stream.concat(userPosts, friendPosts)
                .sorted(COMPARE_DAY_LIKE_COMMENT)
                .filter(post -> postIds.add(post.getId()))
                .filter(Post::isVisible)
                .limit(MAX_FEED_SIZE)
                .map(feedUtil::addViewToPost)
                .map(Mapper::mapPostToDto)
                .collect(Collectors.toList());

        int spotsLeft = MAX_FEED_SIZE - feed.size();
        if (spotsLeft > 0){
            List<PostDto> publicPosts = feedUtil.getPublicPosts()
                    .stream()
                    .filter(post -> postIds.add(post.getId()))
                    .map(Mapper::mapPostToDto)
                    .limit(spotsLeft)
                    .toList();
            feed.addAll(publicPosts);
        }
        return feed;
    }

    /**
     * Retrieves a list of PostDto objects of public posts up to the maximum size of {@value MAX_FEED_SIZE}.
     *
     * @return a list of public PostDto objects for the users public feed.
     */
    public List<PostDto> getPublicFeed() {
        return feedUtil.getPublicPosts()
                .stream()
                .filter(Post::isVisible)
                .limit(MAX_FEED_SIZE)
                .map(feedUtil::addViewToPost)
                .map(Mapper::mapPostToDto)
                .toList();
    }
}
