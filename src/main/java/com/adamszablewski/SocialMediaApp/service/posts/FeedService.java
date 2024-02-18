package com.adamszablewski.SocialMediaApp.service.posts;


import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.utils.FeedUtil;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.adamszablewski.SocialMediaApp.utils.CustomSortingUtil.COMPARE_DAY_LIKE_COMMENT;

@Service
@AllArgsConstructor
public class FeedService {
    private final static int MAX_FEED_SIZE = 100;
    private final FeedUtil feedUtil;
    private final Mapper mapper;

    public List<PostDto> getFeedForUser(long userId) {
        Stream<Post> userPosts = feedUtil.getPostsForUser(userId)
                .stream();
        Stream<Post> friendPosts = feedUtil.getFriendsForUser(userId)
                .stream()
                .map(feedUtil::getPostsForUser)
                .flatMap(List::stream);

        List<PostDto> feed = Stream.concat(userPosts, friendPosts)
                .sorted(COMPARE_DAY_LIKE_COMMENT)
                .map(Mapper::mapPostToDto)
                .limit(MAX_FEED_SIZE)
                .collect(Collectors.toList());

        int spotsLeft = MAX_FEED_SIZE - feed.size();
        if (spotsLeft > 0){
            List<PostDto> publicPosts = feedUtil.getPublicPosts()
                    .stream()
                    .map(Mapper::mapPostToDto)
                    .limit(spotsLeft)
                    .toList();
            feed.addAll(publicPosts);
        }
        return feed;
//        return Stream.concat(
//                feedUtil.getPostsForUser(userId).stream(),
//                feedUtil.getFriendsForUser(userId).stream()
//                        .map(feedUtil::getPostsForUser)
//                        .flatMap(List::stream)
//                )
//                .sorted(COMPARE_DAY_LIKE_COMMENT)
//                .map(Mapper::mapPostToDto)
//                .limit(MAX_FEED_SIZE)
//                .collect(Collectors.toList());
    }


    public List<PostDto> getPublicFeed() {
        return feedUtil.getPublicPosts()
                .stream()
                .map(Mapper::mapPostToDto)
                .toList();

    }
}
