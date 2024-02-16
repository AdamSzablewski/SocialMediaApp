package com.adamszablewski.SocialMediaApp.service.posts;


import com.adamszablewski.SocialMediaApp.dtos.PostDto;
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

        return Stream.concat(
                feedUtil.getPostsForUser(userId).stream(),
                feedUtil.getFriendsForUser(userId).stream()
                        .map(feedUtil::getPostsForUser)
                        .flatMap(List::stream)
                )
                .sorted(COMPARE_DAY_LIKE_COMMENT)
                .map(mapper::mapPostToDto)
                .limit(MAX_FEED_SIZE)
                .collect(Collectors.toList());
    }


}
