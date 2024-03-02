package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.FriendListDto;
import com.adamszablewski.SocialMediaApp.dtos.FriendRequestDto;
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
import java.util.*;

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
    @Test
    void declineFriendRequestTest_shouldDecline(){
        FriendRequest friendRequest = FriendRequest.builder()
                .status(FriendRequestStatus.RECEIVED)
                .build();

        friendService.declineFriendRequest(friendRequest);

        assertThat(friendRequest.getStatus()).isEqualTo(FriendRequestStatus.DECLINED);
        verify(friendRequestRepository).save(friendRequest);
    }

    @Test
    void respondToFriendRequestTest_shouldAccept(){
        FriendRequest friendRequest = FriendRequest.builder()
                .id(1L)
                .status(FriendRequestStatus.RECEIVED)
                .build();
        boolean status = true;

        when(friendRequestRepository.findById(friendRequest.getId())).thenReturn(Optional.of(friendRequest));
        FriendService spy = spy(friendService);
        doNothing().when(spy).acceptFriendRequest(any());
        spy.respondToFriendRequest(friendRequest.getId(), status);

        verify(spy).acceptFriendRequest(any());
    }
    @Test
    void respondToFriendRequestTest_shouldDecline(){
        FriendRequest friendRequest = FriendRequest.builder()
                .id(1L)
                .status(FriendRequestStatus.RECEIVED)
                .build();
        boolean status = false;

        when(friendRequestRepository.findById(friendRequest.getId())).thenReturn(Optional.of(friendRequest));
        FriendService spy = spy(friendService);
        doNothing().when(spy).acceptFriendRequest(any());
        spy.respondToFriendRequest(friendRequest.getId(), status);

        verify(spy).declineFriendRequest(any());
    }
    @Test
    void getFriendRequestsForUser_shouldGet0(){
        long receiverId = 1L;
        when(friendRequestRepository.findAllByReceiverId(receiverId)).thenReturn(new ArrayList<>());

        List<FriendRequestDto> result = friendService.getFriendRequestsForUser(receiverId);

        assertThat(result.size()).isEqualTo(0);
    }
    @Test
    void getFriendRequestsForUser_shouldGet1(){
        long receiverId = 1L;
        FriendRequest friendRequest = FriendRequest.builder()
                .id(1L)
                .status(FriendRequestStatus.RECEIVED)
                .build();
        FriendRequest friendRequestDeclined = FriendRequest.builder()
                .id(1L)
                .status(FriendRequestStatus.DECLINED)
                .build();
        when(friendRequestRepository.findAllByReceiverId(receiverId)).thenReturn(List.of(friendRequest, friendRequestDeclined));

        List<FriendRequestDto> result = friendService.getFriendRequestsForUser(receiverId);

        assertThat(result.size()).isEqualTo(1);
    }
    @Test
    void getFriendRequestsForUser_shouldGet0ForTwoDeclined(){
        long receiverId = 1L;
        FriendRequest friendRequest = FriendRequest.builder()
                .id(1L)
                .status(FriendRequestStatus.DECLINED)
                .build();
        FriendRequest friendRequestDeclined = FriendRequest.builder()
                .id(1L)
                .status(FriendRequestStatus.DECLINED)
                .build();
        when(friendRequestRepository.findAllByReceiverId(receiverId)).thenReturn(List.of(friendRequest, friendRequestDeclined));

        List<FriendRequestDto> result = friendService.getFriendRequestsForUser(receiverId);

        assertThat(result.size()).isEqualTo(0);
    }
    @Test
    void getFriendRequestsForUser_shouldGet1ForTwoAccepted(){
        long receiverId = 1L;
        FriendRequest friendRequest = FriendRequest.builder()
                .id(1L)
                .status(FriendRequestStatus.ACCEPTED)
                .build();
        FriendRequest friendRequestDeclined = FriendRequest.builder()
                .id(1L)
                .status(FriendRequestStatus.ACCEPTED)
                .build();
        when(friendRequestRepository.findAllByReceiverId(receiverId)).thenReturn(List.of(friendRequest, friendRequestDeclined));

        List<FriendRequestDto> result = friendService.getFriendRequestsForUser(receiverId);

        assertThat(result.size()).isEqualTo(0);
    }
    @Test
    void getFriendsForUserIdTest_shouldReturnFriendListOf0(){

        FriendList friendList = FriendList.builder()
                .friends(new ArrayList<>())
                .build();
       Profile profile = Profile.builder()
               .friendList(friendList)
               .build();
        Person person = Person.builder()
                .profile(profile)
                .id(1L)
                .build();

        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        FriendListDto friendListDto = friendService.getFriendsForUserId(person.getId());

        assertThat(friendListDto.getFriends().size()).isEqualTo(0);
    }
    @Test
    void getFriendsForUserIdTest_shouldReturnFriendListOf1(){
        Profile friendProfile = Profile.builder()
                .build();
        FriendList friendList = FriendList.builder()
                .friends(List.of(friendProfile))
                .build();
        Profile profile = Profile.builder()
                .friendList(friendList)
                .build();
        Person person = Person.builder()
                .profile(profile)
                .id(1L)
                .build();

        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        FriendListDto friendListDto = friendService.getFriendsForUserId(person.getId());

        assertThat(friendListDto.getFriends().size()).isEqualTo(1);

    }

    @Test
    void removeFriendTest_shouldRemoveFriend(){

        FriendList friendList1 = FriendList.builder()
                .friends(new ArrayList<>())
                .build();
        FriendList friendList2 = FriendList.builder()
                .friends(new ArrayList<>())
                .build();
        Profile profile1 = Profile.builder()
                .friendList(friendList1)
                .build();
        Profile profile2 = Profile.builder()
                .friendList(friendList2)
                .build();
        Person user1 = Person.builder()
                .profile(profile1)
                .id(1L)
                .build();
        Person user2 = Person.builder()
                .profile(profile2)
                .id(2L)
                .build();
        friendList1.getFriends().add(profile2);
        friendList2.getFriends().add(profile1);
        when(personRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(personRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        friendService.removeFriend(user1.getId(), user2.getId());

        assertThat(user1.getProfile().getFriendList().getFriends().contains(user2.getProfile())).isFalse();
        assertThat(user2.getProfile().getFriendList().getFriends().contains(user1.getProfile())).isFalse();
        verify(friendListRepository, times(2)).save(any());
    }


}
