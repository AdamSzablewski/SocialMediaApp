package com.adamszablewski.SocialMediaApp.dtos;

import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.ProfilePhoto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileDto {
    private long id;
    private Image profilePhoto;
    private PersonDto user;
    private List<PostDto> posts = new ArrayList<>();
    private FriendListDto friendList;
    private List<ConversationDTO> conversations;

}
