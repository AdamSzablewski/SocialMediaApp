package com.adamszablewski.SocialMediaApp.repository.posts;


import com.adamszablewski.SocialMediaApp.enteties.posts.Feed;
import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Upvote, Long> {
    Optional<Feed> findByUserId(long userId);
}
