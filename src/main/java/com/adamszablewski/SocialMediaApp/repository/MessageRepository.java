package com.adamszablewski.SocialMediaApp.repository;


import com.adamszablewski.SocialMediaApp.enteties.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
//    Optional<Message> findByInstanceId(String instanceId);
//
//    Optional<Message> findAllByInstanceId(String instanceId);
//    void  deleteAllByInstanceId(String instanceId);
//
////    Optional<Message> findByInstanceIdAndOwnerId(String instanceId, long ownerId);
//
//    Optional<Message> findByInstanceIdAndSender(String instanceId, long ownerId);
//
//    Optional<Message> findByInstanceIdAndRecipient(String instanceId, Long recipient);
//
//    Optional<Message> findByInstanceIdAndOwner(String instanceId, long ownerId);

//    Optional<Message> findBySender(String sender);
//    Optional<Message> findByReceiver(String sender);
}
