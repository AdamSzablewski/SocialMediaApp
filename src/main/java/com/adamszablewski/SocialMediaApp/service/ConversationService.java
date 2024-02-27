package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.ConversationDTO;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchConversationFoundException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchProfileException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import com.adamszablewski.SocialMediaApp.utils.UserValidator;
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
    private final UserValidator userValidator;
    private final Mapper mapper;
    private final MessageService messageService;


//    public List<Conversation> getCoversationsForUser(long userId) {
//        return conversationRepository.findAllByOwnerId(userId);
//
//    }

    public void deleteConversation(long id) {
        conversationRepository.deleteById(id);
    }

//    public void deleteConversationForUser(Long userId) {
//        List<Conversation> conversations = conversationRepository.findAllByOwnerId(userId);
//        conversations.forEach(conversation -> {
//            conversation.getMessages().forEach(
//                    message -> messageService.deleteMessageFromConversationForUser(conversation, message));
//        });
//    }
    /**
     * Retrieves a Conversation entity between two users based on their IDs.
     * If a conversation between the specified users exists, it is retrieved.
     * If not, a new conversation is created.
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

        for(Conversation conversation : profile1.getConversations()){
            if(profile2.getConversations().contains(conversation)){
                return Mapper.mapConversationToDTO(conversation, Set.of(profile1, profile2), true);
            }
        }

        Conversation newConversation = createConversation(profile1, profile2);
        return Mapper.mapConversationToDTO(newConversation, Set.of(profile1, profile2), true);

    }
    /**
     *Creates a conversation for user ID's in the provided Set
     *
    // * @param users Set of ID's of conversation participants.
     * @return A Conversation object.
     *
     */
    @Transactional
    private Conversation createConversation(Profile profile1, Profile profile2) {
        Conversation conversation = Conversation.builder()
                .isSystemConversation(false)
                .messages(new ArrayList<>())
                .names(getNamesForProfiles(List.of(profile1, profile2)))
                .build();
        conversationRepository.save(conversation);
        profile1.getConversations().add(conversation);
        profile2.getConversations().add(conversation);

        profileRepository.save(profile1);
        profileRepository.save(profile2);

        return conversation;
    }

    public List<ConversationDTO> getAllConversationsForUser(long userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        return profile.getConversations()
                .stream()
                .map(Mapper::mapConversationToDTO)
                .toList();
    }

    public List<String> getNamesForProfiles(List<Profile> profiles){
        List<String> names = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(Profile profile : profiles){
            sb.append(profile.getUser().getFirstName());
            sb.append(" ");
            sb.append(profile.getUser().getLastName());
            sb.append(" ");
            names.add(sb.toString());
            sb.setLength(0);
        }
        return names;
    }

    public ConversationDTO getCoversationById(long conversationId) {
        return Mapper.mapConversationToDTO(conversationRepository.findById(conversationId)
                .orElseThrow(NoSuchConversationFoundException::new));

    }


//    public ResponseEntity<String> createConversation(String user) {
//        conversationCreator.createConversation(user);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//
//    }
//    public Conversation createConversation(String user) {
//        Optional<Conversation> optionalConversation =  conversationRepository.findByParticipantsContains(user);
//        if (optionalConversation.isPresent()){
//            return optionalConversation.get();
//        }
//        Conversation conversation = Conversation.builder()
//                .participants(List.of(user, "support"))
//                .messages(new ArrayList<>())
//                .build();
//        conversationRepository.save(conversation);
//
//        return conversation;
//    }


}
