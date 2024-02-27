package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.UpvoteDto;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchCommentException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchPostException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.interfaces.Likeable;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.CommentRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.LikeRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import lombok.AllArgsConstructor;
import org.hibernate.event.spi.EventType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.CommentEvent;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;

    public boolean checkIfUserAlreadyLiked(Likeable likeableObject, long userId){
        return likeableObject.getLikes().stream()
                .anyMatch(like -> like.getUser().getId() == userId);
    }
    public void removeLike(Likeable likeableObject, long userId){
        Upvote upvote =  likeableObject.getLikes().stream()
                .filter(l -> l.getUser().getId() == userId)
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
        Person user = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Upvote newLike = Upvote.builder()
                .post(post)
                .user(user)
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
        Person user = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Upvote newLike = Upvote.builder()
                .comment(comment)
                .user(user)
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

    public List<UpvoteDto> getLikesForComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NoSuchCommentException::new)
                .getLikes()
                .stream()
                .map(Mapper::mapUpvoteDto)
                .toList();
    }
}
