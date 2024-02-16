package com.adamszablewski.SocialMediaApp.enteties.users;

import com.adamszablewski.SocialMediaApp.enteties.TermsOfUse;
import com.adamszablewski.SocialMediaApp.enteties.posts.Profile;
import com.adamszablewski.SocialMediaApp.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Person implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private LocalDate birthDate;
    private String password;
    private LocalDateTime joinDate;
    private TermsOfUse termsOfUse;
    @OneToOne(cascade = CascadeType.ALL)
    private Profile profile;


    @Override
    public long getId() {
        return this.id;
    }


}
