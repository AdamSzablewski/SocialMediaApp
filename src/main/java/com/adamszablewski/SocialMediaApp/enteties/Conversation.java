package com.adamszablewski.SocialMediaApp.enteties;


import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

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
    @ManyToMany
    @JoinTable(
            name = "conversation_participants",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private Set<Profile> participants = new HashSet<>();


    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", messageCount=" + (messages != null ? messages.size() : 0) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return id == that.id && isSystemConversation == that.isSystemConversation && Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messages, isSystemConversation);
    }
}
