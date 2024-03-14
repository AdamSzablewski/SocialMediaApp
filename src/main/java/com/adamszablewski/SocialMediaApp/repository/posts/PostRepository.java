package com.adamszablewski.SocialMediaApp.repository.posts;

import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> getAllByUserId(Long friend);

    Optional<Post> findByMultimediaId(String multimediaId);

    @Query("SELECT p FROM Post p JOIN p.comments c WHERE c.id = :commentId")
    Optional<Post> findByCommentId(@Param("commentId") long commentId);

    void deleteAllByUserId(long userId);

    void deleteByMultimediaId(String multimediaID);
    @Query("SELECT p FROM Post p WHERE p.isPublic = true AND p.visible = true")
    List<Post> findAllPublicAndVisiblePosts();

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(Long postId);
}
