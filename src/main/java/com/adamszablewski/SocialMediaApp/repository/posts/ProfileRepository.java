package com.adamszablewski.SocialMediaApp.repository.posts;

import com.adamszablewski.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUserId(long userId);

    void deleteByUserId(long userId);
}
