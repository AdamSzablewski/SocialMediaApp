package com.adamszablewski.SocialMediaApp.utils;


import com.adamszablewski.SocialMediaApp.dtos.*;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Message;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequest;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;
import com.adamszablewski.SocialMediaApp.interfaces.Identifiable;
import com.adamszablewski.SocialMediaApp.interfaces.UserResource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
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
                .personDto(mapPersonToDto(post.getPerson(), true))
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
                .firstName(comment.getUser().getFirstName())
                .lastName(comment.getUser().getLastName())
                .text(comment.getText())
                .answers(comment.getAnswers() == null ? new ArrayList<>() : mapCommentToDto(comment.getAnswers()))
                .userId(comment.getUser().getId())
                .likes(comment.getLikes().size())
                .comments(comment.getAnswers().size())
                .build();

    }
    public static List<CommentDto> mapCommentToDto(List<Comment> comments){
        return comments.stream()
                .map(Mapper::mapCommentToDto)
                .collect(Collectors.toList());
    }
    public static UpvoteDto mapUpvoteDto(Upvote upvote){
        return UpvoteDto.builder()
                .user(mapPersonToDto(upvote.getUser()))
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
    public static PersonDto mapPersonToDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .profile(mapProfileToDto(person.getProfile()))
                .birthDate(person.getBirthDate())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birthDate(person.getBirthDate())
                .joinDate(person.getJoinDate().toLocalDate())
                .build();
    }

    public static PersonDto mapPersonToDto(Person person, boolean limited) {
        if(person == null) return null;
        return PersonDto.builder()
                .id(person.getId())
                .birthDate(person.getBirthDate())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birthDate(person.getBirthDate())
                .joinDate(person.getJoinDate().toLocalDate())
                .build();
    }
    public static List<ProfileDto> mapProfileToDto(List<Profile> profiles){
        return profiles.stream()
                .map(Mapper::mapProfileToDto)
                .toList();
    }
    public static Set<ProfileDto> mapProfileToDto(Set<Profile> profiles){
        return profiles.stream()
                .map(Mapper::mapProfileToDto)
                .collect(Collectors.toSet());
    }
    public static List<ProfileDto> mapProfileToDto(List<Profile> profiles, boolean limited){
        return profiles.stream()
                .map(profile -> mapProfileToDto(profile, true))
                .toList();
    }
    public static ProfileDto mapProfileToDto(Profile profile){
        return ProfileDto.builder()
                .profilePhoto(profile.getProfilePhoto())
                .user(mapPersonToDto(profile.getUser(), true))
                .friendList(mapFriendListToDto(profile.getFriendList()))
                .build();
    }

    public static ProfileDto mapProfileToDto(Profile profile, boolean limited){
        return ProfileDto.builder()
                .profilePhoto(profile.getProfilePhoto())
                .user(mapPersonToDto(profile.getUser(), true))
                .build();
    }
    public static FriendListDto mapFriendListToDto(FriendList friendList){
        return FriendListDto.builder()
                .friends(mapProfileToDto(friendList.getFriends(), true))
                .build();
    }
    public static List<Long> getFriendIds(FriendList friendList){
        if(friendList == null){
            return new ArrayList<>();
        }
        List<Long> friendIds = new ArrayList<>();
        friendList.getFriends().forEach(profile -> {
            if(profile.getUser() != null){
                friendIds.add(profile.getId());
            }
        });
        return friendIds;
    }

    public static List<FriendRequestDto> mapFriendRequestToDto(List<FriendRequest> friendRequests) {
        return friendRequests.stream()
                .map(Mapper::mapFriendRequestToDto)
                .toList();
    }
    public static FriendRequestDto mapFriendRequestToDto(FriendRequest friendRequest){
        return FriendRequestDto.builder()
                .id(friendRequest.getId())
                .status(friendRequest.getStatus())
                .sender(mapProfileToDto(friendRequest.getSender()))
                .receiver(mapProfileToDto(friendRequest.getReceiver()))
                .build();
    }

    public static MessageDTO mapMessageToDTO(Message message){
        return MessageDTO.builder()
                .id(message.getId())
                .sender(mapProfileToDto(message.getSender(), true))
                .message(message.getText())
                .dateTime(message.getDateTime())
                .build();
    }
    public static List<MessageDTO> mapMessageToDTO(List<Message> messages){
        return messages.stream()
                .map(Mapper::mapMessageToDTO)
                .toList();
    }

    public static ConversationDTO mapConversationToDTO(Conversation conversation){
        return ConversationDTO.builder()
                .id(conversation.getId())
                .participants(conversation.getNames())
                .messages(mapMessageToDTO(conversation.getMessages()))
                .build();
    }
    public static ConversationDTO mapConversationToDTO(Conversation conversation, Set<Profile> profiles, boolean limited){
        return ConversationDTO.builder()
                .id(conversation.getId())
                .participants(conversation.getNames())
                .messages(mapMessageToDTO(conversation.getMessages()))
                .profiles(mapProfileToDto(profiles))
                .build();
    }

//    public static List<ConversationDTO> mapConversationToDTO(List<Conversation> conversations){
//        List<ConversationDTO> conversationDTOS = new ArrayList<>();
//        conversations.forEach(conversation -> {
//
//            conversationDTOS.add(ConversationDTO.builder()
//                    .id(conversation.getId())
//                    .user(conversation.getUser())
//                    .messages(mapMessageToDTO(conversation.getMessages()))
//                    .build());
//        });
//
//        return conversationDTOS;
//    }
}
