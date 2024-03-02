package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.ConversationDTO;
import com.adamszablewski.SocialMediaApp.dtos.FriendListDto;
import com.adamszablewski.SocialMediaApp.dtos.FriendRequestDto;
import com.adamszablewski.SocialMediaApp.dtos.ProfileDto;
import com.adamszablewski.SocialMediaApp.enteties.Conversation;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequest;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequestStatus;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchConversationFoundException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.ConversationRepository;
import com.adamszablewski.SocialMediaApp.repository.FriendListRepository;
import com.adamszablewski.SocialMediaApp.repository.FriendRequestRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConversationServiceTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private ConversationService conversationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteConversationTest_shouldDeleteConversation(){
        conversationService.deleteConversation(1L);
        verify(conversationRepository).deleteById(1L);
    }
    @Test
    void getConversationsForUser_shouldGet2Conversations(){
        Conversation conversation1 = Conversation.builder()
                .id(1L)
                .participants(new HashSet<>())
                .build();
        Conversation conversation2 = Conversation.builder()
                .id(2L)
                .participants(new HashSet<>())
                .build();
        Profile profile = Profile.builder()
                .conversations(Set.of(conversation1, conversation2))
                .build();
        Person user = Person.builder()
                .id(1L)
                .profile(profile)
                .build();
        conversation1.getParticipants().add(profile);
        conversation2.getParticipants().add(profile);
        when(personRepository.findById(user.getId())).thenReturn(Optional.of(user));

        List<ConversationDTO> result = conversationService.getConversationsForUser(user.getId());

        assertThat(result.size()).isEqualTo(2);
    }
    @Test
    void getConversationsForUser_shouldThrowNoSuchUserException(){


        Person user = Person.builder()
                .id(1L)
                .build();
        when(personRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(NoSuchUserException.class, ()->{
            conversationService.getConversationsForUser(2L);
        });
    }
    @Test
    void getCoversationsBetweenUsersTest_shouldGetConversation(){
        long user1Id = 1L;
        long user2Id = 2L;
        Conversation conversation1 = Conversation.builder()
                .id(1L)
                .participants(new HashSet<>())
                .build();
        Conversation conversation2 = Conversation.builder()
                .id(2L)
                .participants(new HashSet<>())
                .build();
        Profile profile1 = Profile.builder()
                .id(1)
                .conversations(Set.of(conversation1, conversation2))
                .build();
        Profile profile2 = Profile.builder()
                .id(2)
                .conversations(Set.of(conversation1, conversation2))
                .build();

        ProfileDto profile1Dto = Mapper.mapProfileToDto(profile1);
        ProfileDto profile2Dto = Mapper.mapProfileToDto(profile2);

        conversation1.getParticipants().addAll(List.of(profile1, profile2));
        conversation2.getParticipants().add(profile1);
        when(profileRepository.findByUserId(user1Id)).thenReturn(Optional.of(profile1));
        when(profileRepository.findByUserId(user2Id)).thenReturn(Optional.of(profile2));

        ConversationDTO result = conversationService.getCoversationsBetweenUsers(user1Id, user2Id);

        assertThat(result.getParticipants().size()).isEqualTo(2);
        assertTrue(result.getParticipants().containsAll(List.of(profile1Dto, profile2Dto)));
    }
    @Test
    void getCoversationsBetweenUsersTest_shouldThrowNoSuchUserExceptionForUser1(){
        long user1Id = 1L;
        long user2Id = 2L;

        when(profileRepository.findByUserId(user1Id)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, ()-> {
            conversationService.getCoversationsBetweenUsers(user1Id, user2Id);
        });
    }
    @Test
    void getCoversationsBetweenUsersTest_shouldThrowNoSuchUserExceptionForUser2(){
        long user1Id = 1L;
        long user2Id = 2L;
        when(profileRepository.findByUserId(user1Id)).thenReturn(Optional.of(new Profile()));
        when(profileRepository.findByUserId(user1Id)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, ()-> {
            conversationService.getCoversationsBetweenUsers(user1Id, user2Id);
        });
    }
    @Test
    void getCoversationsBetweenUsersTest_shouldCreateConversation(){
        long user1Id = 1L;
        long user2Id = 2L;

        Profile profile1 = Profile.builder()
                .id(1)
                .conversations(new HashSet<>())
                .build();
        Profile profile2 = Profile.builder()
                .id(2)
                .conversations(new HashSet<>())
                .build();
        when(profileRepository.findByUserId(user1Id)).thenReturn(Optional.of(profile1));
        when(profileRepository.findByUserId(user2Id)).thenReturn(Optional.of(profile2));
        Conversation expectedConversation = Conversation.builder().id(1L).build();


        ConversationService spy = spy(conversationService);


        when(spy.createConversation(profile1, profile2)).thenReturn(expectedConversation);

        ConversationDTO result = spy.getCoversationsBetweenUsers(user1Id, user2Id);

        verify(spy).createConversation(eq(profile1), eq(profile2));
        assertEquals(1L, result.getId());
    }
    @Test
    void createConversationTest_shouldCreateConversation(){
        Profile p1 = Profile.builder()
                .id(1L)
                .conversations(new HashSet<>())
                .build();
        Profile p2 = Profile.builder()
                .id(2L)
                .conversations(new HashSet<>())
                .build();

        Conversation result = conversationService.createConversation(p1, p2);

        verify(conversationRepository).save(any());
        assertTrue(result.getParticipants().contains(p1));
        assertTrue(result.getParticipants().contains(p2));
        verify(profileRepository, times(2)).save(any());
    }
    @Test
    void getConversationByIdTest_shouldReturnConversation(){
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(new Conversation()));
        conversationService.getCoversationById(1L);
        verify(conversationRepository).findById(1L);
    }
    @Test
    void getConversationByIdTest_shouldThrowNoSuchUserException(){
        when(conversationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchConversationFoundException.class, ()-> {
            conversationService.getCoversationById(1L);
        });
    }


}
