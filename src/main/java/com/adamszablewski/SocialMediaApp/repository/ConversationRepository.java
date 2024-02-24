package com.adamszablewski.SocialMediaApp.repository;


import com.adamszablewski.SocialMediaApp.dtos.ConversationDTO;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

//    @Query("SELECT c FROM Conversation c WHERE c.participants = :participants")
//    @Query("SELECT c FROM Conversation c " +
//            "WHERE c.participants p = :participantIds")
    List<Conversation> findAllByParticipantsIn(@Param("participants") Set<Long> participants);

    List<Conversation> findAlByParticipantsContaining(@Param("userId") Long userId);

}
