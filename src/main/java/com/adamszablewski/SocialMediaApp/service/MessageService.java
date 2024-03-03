package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.MessageDTO;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Message;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchConversationFoundException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.repository.MessageRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class MessageService {
   private final MessageRepository messageRepository;
   private final ConversationRepository conversationRepository;
   private final PersonRepository personRepository;

 public void addMessageToConversation(long userId, long conversationId){

 }



    public void sendMessageToUserById(long recipientId, long senderId, Message message) {
//        Conversation ownerConversation = conversationRepository.findByOwnerIdAndRecipientId(senderId, recipientId)
//                .orElseGet(()-> conversationCreator.createConversation(senderId, recipientId));
//        Conversation recipientConversation = conversationRepository.findByOwnerIdAndRecipientId(recipientId, senderId)
//                .orElseGet(()-> conversationCreator.createConversation(recipientId, senderId));
//
//        String instanceID = uniqueIdGenerator.generateUniqueId();
//
//        Message m1 = Message.builder()
//                .message(message.getMessage())
//                .dateSent(LocalDateTime.now())
//                .sender(senderId)
//                .recipient(recipientId)
//                .owner(senderId)
//                .conversation(ownerConversation)
//                .instanceId(instanceID)
//                .build();
//        Message m2 = Message.builder()
//                .message(message.getMessage())
//                .sender(senderId)
//                .recipient(recipientId)
//                .owner(recipientId)
//                .conversation(recipientConversation)
//                .dateSent(LocalDateTime.now())
//                .instanceId(instanceID)
//                .build();
//        messageRepository.saveAll(List.of(m1, m2));
//        ownerConversation.getMessages().add(m1);
//        recipientConversation.getMessages().add(m2);
//        conversationRepository.saveAll(Set.of(ownerConversation, recipientConversation));

    }
    public void sendImageToUserById(long recipientId, long senderId, MultipartFile image) throws IOException {
//
//        Conversation ownerConversation = conversationRepository.findByOwnerIdAndRecipientId(senderId, recipientId)
//                .orElseGet(()-> conversationCreator.createConversation(senderId, recipientId));
//        Conversation recipientConversation = conversationRepository.findByOwnerIdAndRecipientId(recipientId, senderId)
//                .orElseGet(()-> conversationCreator.createConversation(recipientId, senderId));
//
//        String instanceID = uniqueIdGenerator.generateUniqueId();
//        String imageId = uniqueIdGenerator.generateUniqueImageId();
//
//        Message m1 = Message.builder()
//                .dateSent(LocalDateTime.now())
//                .sender(senderId)
//                .imageId(imageId)
//                .recipient(recipientId)
//                .owner(senderId)
//                .conversation(ownerConversation)
//                .instanceId(instanceID)
//                .build();
//        Message m2 = Message.builder()
//                .sender(senderId)
//                .imageId(imageId)
//                .recipient(recipientId)
//                .owner(recipientId)
//                .conversation(recipientConversation)
//                .dateSent(LocalDateTime.now())
//                .instanceId(instanceID)
//                .build();
//        byte[] imageData = image.getBytes();
//
//        messageRepository.saveAll(Set.of(m1, m2));
//        ownerConversation.getMessages().add(m1);
//        recipientConversation.getMessages().add(m2);
//        conversationRepository.saveAll(Set.of(ownerConversation, recipientConversation));
//
//        imageServiceClient.sendImageToImageService(imageData, imageId, Set.of(senderId, recipientId));

    }


    public void deleteMessageFromConversationForUser(long conversationId, long messageId) {
//        Conversation conversation = conversationRepository.findById(conversationId)
//                .orElseThrow(NoSuchConversationFoundException::new);
//        Message message = conversation.getMessages().stream()
//                .filter(m -> m.getId() == messageId)
//                .findFirst()
//                .orElseThrow(NoSuchMessageException::new);
//        conversation.getMessages().remove(message);
//        messageRepository.delete(message);
//    }
//    public void deleteMessageFromConversationForUser(Conversation conversation, Message message) {
//        conversation.getMessages().remove(message);
//        messageRepository.delete(message);
    }
    @Transactional
    public void deleteMessageFromConversationForAll(String instanceId, long ownerId) {

//        Message message = messageRepository.findByInstanceIdAndOwner(instanceId, ownerId)
//                .orElseThrow(NoSuchMessageException::new);
//        Message message2 = messageRepository.findByInstanceIdAndOwner(instanceId, message.getRecipient())
//                .orElseThrow(NoSuchMessageException::new);
//        message.getConversation().getMessages().remove(message);
//        message2.getConversation().getMessages().remove(message2);
//
//        messageRepository.deleteAllByInstanceId(instanceId);

    }
//    public void addFriendRequestMessage(FriendRequest friendRequest) {
////        String friendRequestMessageText = "You have a friend request from "+friendRequest.getSenderId();
////        MessageDto messageDto = MessageDto.builder()
////                .message(friendRequestMessageText)
////                .sender(friendRequest.getSenderId())
////                .recipient(friendRequest.getReceiverId())
////                .dateSent(friendRequest.getDateTime())
////                .build();
////        addMessageToConversationFromMessageQueue(messageDto);
//    }
    @Transactional
    public void sendMessageToConversation(long conversationId, long userId, Message messageData) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(NoSuchConversationFoundException::new);
        Person user = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        Message message = Message.builder()
                .text(messageData.getText())
                .dateTime(LocalDateTime.now())
                .sender(user.getProfile())
                .conversation(conversation)
                .build();
        System.out.println(message);
        messageRepository.save(message);
        conversation.getMessages().add(message);
        conversationRepository.save(conversation);
    }
}
