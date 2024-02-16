package com.adamszablewski.SocialMediaApp.enteties.friends;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
public class FriendList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany
    @JoinTable(
            name = "friend_list_friends",
            joinColumns = @JoinColumn(name = "friend_list_id"),
            inverseJoinColumns = @JoinColumn(name = "friends_id")
    )
    private List<Friend> friends;



}
