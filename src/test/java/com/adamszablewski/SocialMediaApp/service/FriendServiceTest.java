package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequest;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendRequestStatus;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.posts.Comment;
import com.adamszablewski.SocialMediaApp.enteties.posts.Post;
import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.FriendListRepository;
import com.adamszablewski.SocialMediaApp.repository.FriendRequestRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.CommentRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.LikeRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FriendServiceTest {

    @Mock
    private FriendListRepository friendListRepository;
    @Mock
    private FriendRequestRepository friendRequestRepository;
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private FriendService friendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendFriendRequestTest_shouldCreateFriendRequest(){
        long senderId = 1L;
        long receiverId = 2L;
        Profile profile1 = Profile.builder()
                .id(senderId)
                .build();
        Profile profile2 = Profile.builder()
                .id(receiverId)
                .build();
        Person user1 = Person.builder()
                .id(senderId)
                .profile(profile1)
                .build();
        Person user2 = Person.builder()
                .id(receiverId)
                .profile(profile2)
                .build();
        ArgumentCaptor<FriendRequest> argumentCaptor = ArgumentCaptor.forClass(FriendRequest.class);
        when(personRepository.findById(senderId)).thenReturn(Optional.of(user1));
        when(personRepository.findById(receiverId)).thenReturn(Optional.of(user2));

        friendService.sendFriendRequest(senderId, receiverId);

        verify(friendRequestRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getStatus()).isEqualTo(FriendRequestStatus.RECEIVED);
        assertThat(argumentCaptor.getValue().getDateTime()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(argumentCaptor.getValue().getSender()).isEqualTo(user1.getProfile());
        assertThat(argumentCaptor.getValue().getReceiver()).isEqualTo(user2.getProfile());
    }
    @Test
    void sendFriendRequestTest_shouldThrowNoSuchUserExceptionForMissingSender(){
        long senderId = 1L;
        long receiverId = 2L;
        when(personRepository.findById(senderId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, ()-> {
            friendService.sendFriendRequest(senderId, receiverId);
        });
    }
    @Test
    void sendFriendRequestTest_shouldThrowNoSuchUserExceptionForMissingReceiver(){
        long senderId = 1L;
        long receiverId = 2L;
        Profile profile1 = Profile.builder()
                .id(senderId)
                .build();
        Person user1 = Person.builder()
                .id(senderId)
                .profile(profile1)
                .build();
        when(personRepository.findById(senderId)).thenReturn(Optional.of(user1));
        when(personRepository.findById(senderId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, ()-> {
            friendService.sendFriendRequest(senderId, receiverId);
        });
    }
    @Test
    public void acceptFriendRequestTest_shouldAccept(){

        Profile profile1 = Profile.builder()
                .id(1L)
                .friendList(
                        FriendList.builder()
                        .friends(new ArrayList<>())
                        .build()
                )
                .build();
        Profile profile2 = Profile.builder()
                .id(2L)
                .friendList(
                        FriendList.builder()
                        .friends(new ArrayList<>())
                        .build()
                )
                .build();
        FriendRequest friendRequest = FriendRequest.builder()
                .status(FriendRequestStatus.RECEIVED)
                .sender(profile1)
                .receiver(profile2)
                .build();
        FriendRequest newFriendRequest = FriendRequest.builder()
                .status(FriendRequestStatus.ACCEPTED)
                .sender(profile1)
                .receiver(profile2)
                .build();

        friendService.acceptFriendRequest(friendRequest);

        verify(friendListRepository, times(2)).save(any());
        assertThat(profile2.getFriendList().getFriends().get(0).getId()).isEqualTo(1L);
        assertThat(profile1.getFriendList().getFriends().get(0).getId()).isEqualTo(2L);
        verify(friendRequestRepository).save(eq(newFriendRequest));
    }

}
