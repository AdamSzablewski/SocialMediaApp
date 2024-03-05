package com.adamszablewski.SocialMediaApp.enteties.friends;

import com.adamszablewski.SocialMediaApp.enteties.Person;
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
    @OneToOne
    private Person user;
    @ManyToMany
    @JoinTable(
            name = "friend_list_friends",
            joinColumns = @JoinColumn(name = "friend_list_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private List<Profile> friends;




}
