package com.adamszablewski.SocialMediaApp.service.posts;


import com.adamszablewski.SocialMediaApp.dtos.CommentDto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchCommentException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchPostException;
import com.adamszablewski.SocialMediaApp.repository.posts.CommentRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import lombok.AllArgsConstructor;
import org.hibernate.event.spi.EventType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.CommentEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void deleteCommentForPost(long postId, long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchCommentException::new);
        Comment commentToRemove = post.getComments().stream()
                .filter(comment -> comment.getId() == commentId)
                .findFirst()
                .orElseThrow(NoSuchCommentException::new);
        post.getComments().remove(commentToRemove);
        commentRepository.delete(commentToRemove);
    }
    @Transactional
    public void deleteCommentForComment (long parentCommentId, long commentId) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(NoSuchCommentException::new);
        Comment commentToRemove = parentComment.getComments().stream()
                .filter(comment -> comment.getId() == commentId)
                .findFirst()
                .orElseThrow(NoSuchCommentException::new);
        parentComment.getAnswers().remove(commentToRemove);
        commentRepository.delete(commentToRemove);
    }
    @Transactional
    public void postCommentForPost(long postId, Comment commentData) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);
        Comment comment = Comment.builder()
                .text(commentData.getText())
                .userId(commentData.getUserId())
                .dateTime(LocalDateTime.now())
                .build();
        post.getComments().add(comment);
        commentRepository.save(comment);
        postRepository.save(post);
    }
    @Transactional
    public void postCommentForComment(long commentId, Comment commentData, long userId) {
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(NoSuchCommentException::new);
        Comment comment = Comment.builder()
                .text(commentData.getText())
                .userId(userId)
                .dateTime(LocalDateTime.now())
                .build();

        if (comment.getAnswers() == null){
            parent.setAnswers(new ArrayList<>());
        }
        parent.getAnswers().add(comment);
        commentRepository.save(comment);
        commentRepository.save(parent);
    }

    public List<CommentDto> getCommentsForPost(long postId) {
        return postRepository.findById(postId)
                .map(Post::getComments)
                .orElse(List.of())
                .stream()
                .map(Mapper::mapCommentToDto)
                .toList();
    }
}