package com.adamszablewski.SocialMediaApp.service.posts;

import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchCommentException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchPostException;
import com.adamszablewski.SocialMediaApp.interfaces.Likeable;
import com.adamszablewski.SocialMediaApp.repository.posts.CommentRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.LikeRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import lombok.AllArgsConstructor;
import org.hibernate.event.spi.EventType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.CommentEvent;

@Service
@AllArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public boolean checkIfUserAlreadyLiked(Likeable likeableObject, long userId){
        return likeableObject.getLikes().stream()
                .anyMatch(like -> like.getUserId() == userId);
    }
    public void removeLike(Likeable likeableObject, long userId){
        Upvote upvote =  likeableObject.getLikes().stream()
                .filter(l -> l.getUserId() == userId)
                .findFirst()
                .orElse(null);
        if (upvote != null){
            likeableObject.getLikes().remove(upvote);
            likeRepository.delete(upvote);
        }
    }
    @Transactional
    public void likePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);
        Upvote newLike = Upvote.builder()
                .post(post)
                .userId(userId)
                .build();
        boolean alreadyLiked = checkIfUserAlreadyLiked(post, userId);
        if(!alreadyLiked) {
            post.getLikes().add(newLike);
            likeRepository.save(newLike);
            postRepository.save(post);
        }
    }
    @Transactional
    public void likeComment(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NoSuchPostException::new);
        Upvote newLike = Upvote.builder()
                .comment(comment)
                .userId(userId)
                .build();
        boolean alreadyLiked = checkIfUserAlreadyLiked(comment, userId);
        if(!alreadyLiked) {
            comment.getLikes().add(newLike);
            likeRepository.save(newLike);
            commentRepository.save(comment);
        }
    }


    public void unLikePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);
        removeLike(post, userId);
        postRepository.save(post);

    }

    public void unLikeComment(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NoSuchCommentException::new);
        removeLike(comment, userId);
        commentRepository.save(comment);
    }
}
