package com.adamszablewski.SocialMediaApp.enteties.friends;

import com.adamszablewski.SocialMediaApp.interfaces.Identifiable;
import com.adamszablewski.SocialMediaApp.interfaces.UserResource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Friend implements Identifiable, UserResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    @OneToOne(cascade = CascadeType.ALL)
    private FriendList friendList;
    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", userId=" + userId +
                '}';
    }

}
