package com.adamszablewski.SocialMediaApp.enteties.friends;

import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @OneToOne(cascade = CascadeType.ALL)
    private Image profilePhoto;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private FriendList friendList;


    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", user=" + user.getFirstName()+" "+user.getLastName() +
                ", profilePhoto=" + profilePhoto +
                '}';
    }
}
