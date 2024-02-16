package com.adamszablewski.SocialMediaApp.enteties.posts;

import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
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
    @OneToOne(cascade = CascadeType.ALL)
    private Image profilePhoto;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();


}
