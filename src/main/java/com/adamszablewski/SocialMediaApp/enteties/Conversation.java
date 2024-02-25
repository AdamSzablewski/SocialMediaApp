package com.adamszablewski.SocialMediaApp.enteties;


import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Conversation  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Message> messages = new ArrayList<>();
    private boolean isSystemConversation;
    private List<String> names;


    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", messageCount=" + (messages != null ? messages.size() : 0) +
                '}';
    }

}
