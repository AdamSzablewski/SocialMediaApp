package com.adamszablewski.SocialMediaApp.repository.posts;


import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Set<Profile> findAllByIdIn(Set<Long> ids);

    Optional<Profile> findByUserId(long user1Id);
}
