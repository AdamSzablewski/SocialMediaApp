package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.MessageDTO;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Message;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Video;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchConversationFoundException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchMessageException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.repository.MessageRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class MessageService {
   private final MessageRepository messageRepository;
   private final ConversationService conversationService;
   private final ConversationRepository conversationRepository;
   private final PersonRepository personRepository;
   private final ImageService imageService;
   @Transactional
   public Message addTextMessageToConversation(long userId, long conversationId, MessageDTO messageDTO) throws Exception {
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Conversation conversation = conversationService.getConversationByIdFromUser(person.getProfile(), conversationId);
        Message message = Message.builder()
                .sender(person.getProfile())
                .dateTime(LocalDateTime.now())
                .build();
        message.setEncryptedMessage(messageDTO.getMessage());
        conversation.getMessages().add(message);
        messageRepository.save(message);
        conversationRepository.save(conversation);
        return message;
    }
    @Transactional
    public Message addImageMessageToConversation(long userId, long conversationId, MultipartFile file) {
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Conversation conversation = conversationService.getConversationByIdFromUser(person.getProfile(), conversationId);
        Image image = imageService.createPhotoResource(file, userId);
        Message message = Message.builder()
                .image(image)
                .sender(person.getProfile())
                .dateTime(LocalDateTime.now())
                .build();
        conversation.getMessages().add(message);
        messageRepository.save(message);
        conversationRepository.save(conversation);
        return message;
    }
    @Transactional
    public Message addVideoMessageToConversation(long userId, long conversationId, MultipartFile file) {
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Conversation conversation = conversationService.getConversationByIdFromUser(person.getProfile(), conversationId);
        Video video = imageService.createVideoResource(file, userId);
        Message message = Message.builder()
                .video(video)
                .sender(person.getProfile())
                .dateTime(LocalDateTime.now())
                .build();
        conversation.getMessages().add(message);
        messageRepository.save(message);
        conversationRepository.save(conversation);
        return message;
    }
    @Transactional
    public void deleteMessage(long conversationId, long messageId) {
       Conversation conversation = conversationRepository.findById(conversationId)
               .orElseThrow(NoSuchConversationFoundException::new);
       Message message = getMessageFromConversation(conversation, messageId);
       conversation.getMessages().remove(message);
       conversationRepository.save(conversation);
       messageRepository.delete(message);
    }
    public Message getMessageFromConversation(Conversation conversation, long messageId){
       return conversation.getMessages().stream()
               .filter(message -> message.getId() == messageId)
               .findFirst()
               .orElseThrow(NoSuchMessageException::new);
    }
}
