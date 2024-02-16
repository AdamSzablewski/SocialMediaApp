package com.adamszablewski.SocialMediaApp.repository.posts;

import com.adamszablewski.model.Feed;
import com.adamszablewski.model.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Upvote, Long> {
    Optional<Feed> findByUserId(long userId);
}
