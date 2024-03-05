package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.ConversationDTO;
import com.adamszablewski.SocialMediaApp.dtos.MessageDTO;
import com.adamszablewski.SocialMediaApp.dtos.ProfileDto;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Message;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Video;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchConversationFoundException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.repository.ImageRepositroy;
import com.adamszablewski.SocialMediaApp.repository.MessageRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.utils.EncryptionUtil;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessagingServiceTest {
    @Mock
    private ConversationService conversationService;

    @Mock
    private ImageService imageService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   @Test
    void addTextMessageToConversationTest_shouldAddTextMessage() throws Exception {
        long personId = 1L;
        long conversationId = 1L;
        Profile profile = Profile.builder()
                .id(1L)
                .build();
        Person person = Person.builder()
                .id(personId)
                .profile(profile)
                .build();
        Conversation conversation = Conversation.builder()
                .id(1L)
                .messages(new ArrayList<>())
                .isSystemConversation(false)
                .participants(List.of(profile))
                .build();
       MessageDTO message = MessageDTO.builder()
               .id(1L)
               .message("Test")
               .build();

       when(personRepository.findById(personId)).thenReturn(Optional.of(person));
       when(conversationService.getConversationByIdFromUser(profile, conversationId)).thenReturn(conversation);
       Message result = messageService.addTextMessageToConversation(personId, conversationId, message);

       assertTrue(conversation.getMessages().contains(result));
       assertEquals(result.getText(), message.getMessage());
       assertEquals(result.getSender(), profile);
       assertThat(result.getDateTime()).isEqualToIgnoringSeconds(LocalDateTime.now());
       verify(messageRepository).save(result);
       verify(conversationRepository).save(conversation);
   }
    @Test
    void addTextMessageToConversationTest_shouldThrowNoSuchUserExc(){
        long personId = 1L;
        long conversationId = 1L;
        when(personRepository.findById(personId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, ()->{
            messageService.addTextMessageToConversation(personId, conversationId, new MessageDTO());
        });
    }
    @Test
    void addImageMessageToConversationTest_shouldAddImageMessage(){
        long personId = 1L;
        long conversationId = 1L;
        Profile profile = Profile.builder()
                .id(1L)
                .build();
        Person person = Person.builder()
                .id(personId)
                .profile(profile)
                .build();
        Conversation conversation = Conversation.builder()
                .id(1L)
                .messages(new ArrayList<>())
                .isSystemConversation(false)
                .participants(List.of(profile))
                .build();

        MultipartFile file = new MockMultipartFile("filename",  new byte[1]);
        Image image = Image.builder()
                .multimediaId("IMAGE12345")
                .build();
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(conversationService.getConversationByIdFromUser(profile, conversationId)).thenReturn(conversation);
        when(imageService.createPhotoResource(file, personId)).thenReturn(image);


        Message result = messageService.addImageMessageToConversation(personId, conversationId, file);

        assertTrue(conversation.getMessages().contains(result));
        assertTrue(result.getImage().getMultimediaId().startsWith("IMAGE"));
        assertEquals(result.getSender(), profile);
        assertThat(result.getDateTime()).isEqualToIgnoringSeconds(LocalDateTime.now());
        verify(messageRepository).save(result);
        verify(conversationRepository).save(conversation);
    }
    @Test
    void addImageMessageToConversationTest_shouldThrowNoSuchUserExc(){
        long personId = 1L;
        long conversationId = 1L;
        when(personRepository.findById(personId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, ()->{
            messageService.addImageMessageToConversation(personId, conversationId, new MockMultipartFile("test", new byte[1]));
        });
    }
    @Test
    void addVideoMessageToConversationTest_shouldAddVideoMessage(){
        long personId = 1L;
        long conversationId = 1L;
        Profile profile = Profile.builder()
                .id(1L)
                .build();
        Person person = Person.builder()
                .id(personId)
                .profile(profile)
                .build();
        Conversation conversation = Conversation.builder()
                .id(1L)
                .messages(new ArrayList<>())
                .isSystemConversation(false)
                .participants(List.of(profile))
                .build();

        MultipartFile file = new MockMultipartFile("filename",  new byte[1]);
        Video video = Video.builder()
                .multimediaId("VIDEO12345")
                .build();
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(conversationService.getConversationByIdFromUser(profile, conversationId)).thenReturn(conversation);
        when(imageService.createVideoResource(file, personId)).thenReturn(video);


        Message result = messageService.addVideoMessageToConversation(personId, conversationId, file);

        assertTrue(conversation.getMessages().contains(result));
        assertTrue(result.getVideo().getMultimediaId().startsWith("VIDEO"));
        assertEquals(result.getSender(), profile);
        assertThat(result.getDateTime()).isEqualToIgnoringSeconds(LocalDateTime.now());
        verify(messageRepository).save(result);
        verify(conversationRepository).save(conversation);
    }
    @Test
    void addVideoMessageToConversationTest_shouldThrowNoSuchUserExc(){
        long personId = 1L;
        long conversationId = 1L;
        when(personRepository.findById(personId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, ()->{
            messageService.addVideoMessageToConversation(personId, conversationId, new MockMultipartFile("test", new byte[1]));
        });
    }

}
