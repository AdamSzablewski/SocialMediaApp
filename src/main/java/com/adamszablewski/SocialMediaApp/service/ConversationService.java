package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.ConversationDTO;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
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
     * @throws IllegalArgumentException if user1Id or user2Id is invalid (e.g., negative).
     */
    public ConversationDTO getCoversationsBetweenUsers(long user1Id, long user2Id) {
        Set<Long> users = new HashSet<>();
        users.add(user1Id);
        users.add(user2Id);
        List<Conversation> conversations= conversationRepository.findAllByParticipantsIn(users);
        Conversation foundConversation = conversations.stream()
                .filter(conversation -> conversation.getParticipants().contains(user1Id) && conversation.getParticipants().contains(user2Id))
                .findFirst()
                .orElseGet( ()-> createConversation(users));
        return Mapper.mapConversationToDTO(foundConversation);

    }
    /**
     *Creates a conversation for user ID's in the provided Set
     *
     * @param users Set of ID's of conversation participants.
     * @return A Conversation object.
     *
     */
    @Transactional
    private Conversation createConversation(Set<Long> users) {
        System.out.println("creating called");
        Conversation conversation = Conversation.builder()
                .isSystemConversation(false)
                .participants(users)
                .build();
        conversationRepository.save(conversation);
        users.forEach(userId -> {
            Profile profile = profileRepository.findByUserId(userId)
                    .orElseThrow(NoSuchUserException::new);
            conversation.getProfiles().add(profile);
        });
        System.out.println(conversation);
        return conversation;
    }

    public List<ConversationDTO> getAllConversationsForUser(long userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        return conversationRepository.findAlByParticipantsContaining(userId)
                .stream()
                .map(Mapper::mapConversationToDTO)
                .toList();
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
