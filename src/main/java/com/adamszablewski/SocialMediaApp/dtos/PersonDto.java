package com.adamszablewski.SocialMediaApp.dtos;

import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class PersonDto {

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate joinDate;
    private LocalDate birthDate;
    private ProfileDto profile;
    private Image profilePhoto;
}
