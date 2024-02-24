package com.adamszablewski.SocialMediaApp.enteties;


import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ElementCollection
    private Set<Long> participants;
    @ManyToMany
    @JoinTable(
            name = "conversation_profile",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private Set<Profile> profiles;
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Message> messages;
    private boolean isSystemConversation;


    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", messageCount=" + (messages != null ? messages.size() : 0) +
                '}';
    }

}
