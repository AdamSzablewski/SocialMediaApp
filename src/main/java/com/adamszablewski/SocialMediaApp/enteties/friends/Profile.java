package com.adamszablewski.SocialMediaApp.enteties.friends;

import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.ProfilePhoto;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedNotification;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Person user;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Image profilePhoto;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private FriendList friendList;
    @ManyToMany
    private List<Conversation> conversations = new ArrayList<>();


    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", user=" + Mapper.mapPersonToDto(user, true) +
                ", profilePhoto=" + profilePhoto +
                ", posts=" + Mapper.mapPostToDto(posts) +
                //", friendList=" + friendList +
                ", conversations=" + Mapper.mapConversationToDTO(conversations, true) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return id == profile.id && Objects.equals(user, profile.user) && Objects.equals(profilePhoto, profile.profilePhoto) && Objects.equals(posts, profile.posts) && Objects.equals(friendList, profile.friendList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, profilePhoto, posts, friendList);
    }
}
