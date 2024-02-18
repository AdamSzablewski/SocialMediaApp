package com.adamszablewski.SocialMediaApp.repository.posts;


import com.adamszablewski.SocialMediaApp.enteties.posts.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {


}
