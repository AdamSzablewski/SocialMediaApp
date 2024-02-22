package com.adamszablewski.SocialMediaApp.repository;

import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendListRepository extends JpaRepository<FriendList, Long> {

}
