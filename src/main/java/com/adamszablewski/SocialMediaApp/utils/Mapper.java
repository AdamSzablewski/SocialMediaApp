package com.adamszablewski.SocialMediaApp.utils;


import com.adamszablewski.SocialMediaApp.dtos.CommentDto;
import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.dtos.UpvoteDto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;
import com.adamszablewski.SocialMediaApp.interfaces.Identifiable;
import com.adamszablewski.SocialMediaApp.interfaces.UserResource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Mapper {
    public <T extends Identifiable> Set<Long> convertObjectListToIdSet(Collection<T> collection){
        return collection.stream()
                .map(Identifiable::getId)
                .collect(Collectors.toSet());
    }
    public <T extends Identifiable> Long convertObjectToId(T entity){
        return entity.getId();
    }
    public <T extends UserResource> Long getUserId(T userResource) {
        return userResource.getUserId();
    }


    public static  List<PostDto> mapPostToDto(List<Post> posts) {
        return posts.stream()
                .map(Mapper::mapPostToDto)
                .collect(Collectors.toList());
    }
    public static PostDto mapPostToDto(Post post){
        return PostDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .userLikeIds(post.getLikes() == null ? new HashSet<>() : mapUpvoteDto(post.getLikes()))
                .likes(countLikes(post))
                .comments(post.getComments() == null ? new ArrayList<>() : mapCommentToDto(post.getComments()))
                .text(post.getText())
                .multimediaId(post.getMultimediaId())
                .description(post.getDescription())
                .build();
    }
    public static CommentDto mapCommentToDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .answers(comment.getAnswers() == null ? new ArrayList<>() : mapCommentToDto(comment.getAnswers()))
                .userId(comment.getUserId())
                .likes(mapUpvoteDto(comment.getLikes()))
                .build();

    }
    public static List<CommentDto> mapCommentToDto(List<Comment> comments){
        return comments.stream()
                .map(Mapper::mapCommentToDto)
                .collect(Collectors.toList());
    }
    public static UpvoteDto mapUpvoteDto(Upvote upvote){
        return UpvoteDto.builder()
                .userId(upvote.getUserId())
                .build();
    }
    public static Set<UpvoteDto> mapUpvoteDto(Set<Upvote> upvotes){
        return upvotes.stream()
                .map(Mapper::mapUpvoteDto)
                .collect(Collectors.toSet());
    }
    public static int countLikes(Post post){
        return post.getLikes().size();
    }

}
