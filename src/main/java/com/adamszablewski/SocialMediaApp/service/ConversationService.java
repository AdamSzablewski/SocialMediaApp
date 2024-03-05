package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.ConversationDTO;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchConversationFoundException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

//import static com.adamszablewski.util.Mapper.mapConversationToDTO;

@AllArgsConstructor
@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ProfileRepository profileRepository;
    private final PersonRepository personRepository;


    public List<ConversationDTO> getConversationsForUser(long userId) {
        Person user = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        return user.getProfile().getConversations().stream()
                .map(Mapper::mapConversationToDTO)
                .toList();
    }

    public void deleteConversation(long id) {
        conversationRepository.deleteById(id);
    }



    /**
     * Finds a Conversation entity by retrieving a Profile entity for user1 and finding
     * the associated Conversation held with User2. THe result is mapped to ConversationDTO
     *
     * @param user1Id The ID of the first user.
     * @param user2Id The ID of the second user.
     * @return A ConversationDTO representing the conversation between the two users.
     */
    public ConversationDTO getCoversationsBetweenUsers(long user1Id, long user2Id) {

        Profile profile1 = profileRepository.findByUserId(user1Id)
                .orElseThrow(NoSuchUserException::new);
        Profile profile2 = profileRepository.findByUserId(user2Id)
                .orElseThrow(NoSuchUserException::new);
        Conversation conversation = profile1.getConversations().stream()
                .filter(conv -> conv.getParticipants() !=  null && conv.getParticipants().contains(profile2) && conv.getParticipants().size()==2)
                .findFirst()
                .orElseGet(() -> createConversation(profile1, profile2));
        return Mapper.mapConversationToDTO(conversation, true);

    }
    /**
     *Creates a conversation for user ID's in the provided Set
     *
    // * @param users Set of ID's of conversation participants.
     * @return A Conversation object.
     *
     */
    @Transactional
    public Conversation createConversation(Profile profile1, Profile profile2) {
        Conversation conversation = Conversation.builder()
                .isSystemConversation(false)
                .messages(new ArrayList<>())
                .participants(new ArrayList<>())
                .build();
        conversationRepository.save(conversation);
        profile1.getConversations().add(conversation);
        profile2.getConversations().add(conversation);
        profileRepository.save(profile1);
        profileRepository.save(profile2);
        conversation.getParticipants().add(profile1);
        conversation.getParticipants().add(profile2);

        conversationRepository.save(conversation);

        return conversation;
    }

    public ConversationDTO getCoversationById(long conversationId) {
        return Mapper.mapConversationToDTO(conversationRepository.findById(conversationId)
                .orElseThrow(NoSuchConversationFoundException::new));
    }
    @Transactional
    public void addUserToExistingConversation(long conversationId, long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(NoSuchConversationFoundException::new);
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Profile profile = person.getProfile();

        conversation.getParticipants().add(profile);
        profile.getConversations().add(conversation);

        conversationRepository.save(conversation);
        profileRepository.save(profile);
    }
    @Transactional
    public void removeUserFromConversation(long conversationId, long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(NoSuchConversationFoundException::new);
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Profile profile = person.getProfile();

        conversation.getParticipants().remove(profile);
        profile.getConversations().remove(conversation);
        profileRepository.save(profile);

        if (conversation.getParticipants().isEmpty()){
            conversationRepository.delete(conversation);
        }
        conversationRepository.save(conversation);
    }

    /**
     * Finds the Conversation entity that has the correct conversationId from user conversations.
     * @param profile
     * @param conversationId
     * @return Conversation
     */
    public Conversation getConversationByIdFromUser(Profile profile, long conversationId) {
        return profile.getConversations().stream()
                .filter(conversation -> conversation.getId() == conversationId)
                .findFirst()
                .orElseThrow(NoSuchConversationFoundException::new);
    }
}
