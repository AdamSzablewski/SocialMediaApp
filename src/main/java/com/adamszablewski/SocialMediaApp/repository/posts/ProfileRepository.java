package com.adamszablewski.SocialMediaApp.repository.posts;


import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {


}
