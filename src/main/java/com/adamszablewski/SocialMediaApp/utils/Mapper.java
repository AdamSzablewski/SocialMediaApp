package com.adamszablewski.SocialMediaApp.utils;


import com.adamszablewski.SocialMediaApp.dtos.*;
import com.adamszablewski.SocialMediaApp.dtos.message.MessageDTO;
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


    public static <T extends Identifiable> Set<Long> convertObjectListToIdSet(Collection<T> collection){
        return collection.stream()
                .map(Identifiable::getId)
                .collect(Collectors.toSet());
    }
    public static <T extends Identifiable> Long convertObjectToId(T entity){
        return entity.getId();
    }
    public static <T extends UserResource> Long getUserId(T userResource) {
        return userResource.getUserId();
    }


    public static  List<PostDto> mapPostToDto(List<Post> posts) {
        if(posts == null) return null;
        return posts.stream()
                .map(Mapper::mapPostToDto)
                .collect(Collectors.toList());
    }

    public static PostDto mapPostToDto(Post post){
        if (post == null){
            return null;
        }
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
                .viewCount(post.getViewCount())
                .build();
    }
    public static CommentDto mapCommentToDto(Comment comment){
        if (comment == null){
            return null;
        }
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
        if (comments == null) return null;
        return comments.stream()
                .map(Mapper::mapCommentToDto)
                .collect(Collectors.toList());
    }
    public static UpvoteDto mapUpvoteDto(Upvote upvote){
        if (upvote == null) return null;
        return UpvoteDto.builder()
                .user(mapPersonToDto(upvote.getUser()))
                .build();
    }
    public static Set<UpvoteDto> mapUpvoteDto(Set<Upvote> upvotes){
        if (upvotes == null) return null;
        return upvotes.stream()
                .map(Mapper::mapUpvoteDto)
                .collect(Collectors.toSet());
    }
    public static int countLikes(Post post){
        if (post == null) return 0;
        return post.getLikes().size();
    }
    public static PersonDto mapPersonToDto(Person person) {
        if (person == null) return null;
        return PersonDto.builder()
                .id(person.getId())
                .profile(mapProfileToDto(person.getProfile()))
                .birthDate(person.getBirthDate())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birthDate(person.getBirthDate())
                .profilePhoto(person.getProfile().getProfilePhoto())
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
                .profilePhoto(person.getProfile().getProfilePhoto())
                .birthDate(person.getBirthDate())
                .joinDate(person.getJoinDate().toLocalDate())
                .build();
    }
    public static List<ProfileDto> mapProfileToDto(List<Profile> profiles){
        if (profiles == null) return null;
        return profiles.stream()
                .map(Mapper::mapProfileToDto)
                .toList();
    }
    public static Set<ProfileDto> mapProfileToDto(Set<Profile> profiles){
        if (profiles == null) return null;
        return profiles.stream()
                .map(Mapper::mapProfileToDto)
                .collect(Collectors.toSet());
    }
    public static List<ProfileDto> mapProfileToDto(List<Profile> profiles, boolean limited){
        if (profiles == null) return null;
        return profiles.stream()
                .map(profile -> mapProfileToDto(profile, true))
                .toList();
    }
    public static ProfileDto mapProfileToDto(Profile profile){
        if (profile == null) return null;
        return ProfileDto.builder()
                .id(profile.getId())
                .profilePhoto(profile.getProfilePhoto())
                .user(mapPersonToDto(profile.getUser(), true))
                .friendList(mapFriendListToDto(profile.getFriendList()))
                .build();
    }

    public static ProfileDto mapProfileToDto(Profile profile, boolean limited){
        if (profile == null) return null;
        return ProfileDto.builder()
                .profilePhoto(profile.getProfilePhoto())
                .user(mapPersonToDto(profile.getUser(), true))
                .build();
    }
    public static FriendListDto mapFriendListToDto(FriendList friendList){
        if (friendList == null) return null;
        return FriendListDto.builder()
                .friends(mapProfileToDto(friendList.getFriends(), true))
                .build();
    }
    public static List<Long> getFriendIds(FriendList friendList){
        if(friendList == null) return null;
        List<Long> friendIds = new ArrayList<>();
        friendList.getFriends().forEach(profile -> {
            if(profile.getUser() != null){
                friendIds.add(profile.getId());
            }
        });
        return friendIds;
    }

    public static List<FriendRequestDto> mapFriendRequestToDto(List<FriendRequest> friendRequests) {
        if (friendRequests == null) return null;
        return friendRequests.stream()
                .map(Mapper::mapFriendRequestToDto)
                .toList();
    }
    public static FriendRequestDto mapFriendRequestToDto(FriendRequest friendRequest){
        if (friendRequest == null) return null;
        return FriendRequestDto.builder()
                .id(friendRequest.getId())
                .status(friendRequest.getStatus())
                .sender(mapProfileToDto(friendRequest.getSender()))
                .receiver(mapProfileToDto(friendRequest.getReceiver()))
                .build();
    }

    public static MessageDTO mapMessageToDTO(Message message){
        if (message == null) return null;
        return MessageDTO.builder()
                .id(message.getId())
                .sender(mapProfileToDto(message.getSender(), true))
                .message(message.getText())
                .dateTime(message.getDateTime())
                .build();
    }
    public static List<MessageDTO> mapMessageToDTO(List<Message> messages){
        if(messages == null) return null;
        return messages.stream()
                .map(Mapper::mapMessageToDTO)
                .toList();
    }

    public static ConversationDTO mapConversationToDTO(Conversation conversation){
        if (conversation == null) return null;
        return ConversationDTO.builder()
                .id(conversation.getId())
                .participants(mapProfileToDto(conversation.getParticipants()))
                .messages(mapMessageToDTO(conversation.getMessages()))
                .build();
    }
    public static ConversationDTO mapConversationToDTO(Conversation conversation, boolean limited){
        if (conversation == null)  return null;
        return ConversationDTO.builder()
                .id(conversation.getId())
                .participants(mapProfileToDto(conversation.getParticipants()))
                .messages(mapMessageToDTO(conversation.getMessages()))
                .build();
    }
    public static List<ConversationDTO> mapConversationToDTO(List<Conversation> conversations, boolean limited){
        if (conversations == null) return null;
        return conversations.stream()
                .map(Mapper::mapConversationToDTO)
                .toList();
    }
}
