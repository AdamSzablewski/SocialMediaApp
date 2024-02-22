package com.adamszablewski.SocialMediaApp.repository;

import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByReceiverId(long userId);


}
