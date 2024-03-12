package com.adamszablewski.SocialMediaApp.service;


import com.adamszablewski.SocialMediaApp.dtos.FriendListDto;
import com.adamszablewski.SocialMediaApp.dtos.FriendRequestDto;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.*;
import com.adamszablewski.SocialMediaApp.exceptions.NoFriendRequestException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.FriendListRepository;
import com.adamszablewski.SocialMediaApp.repository.FriendRequestRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor

public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendListRepository friendListRepository;
    private final ProfileRepository profileRepository;
    private final PersonRepository personRepository;
    private final Mapper mapper;

    private void addFriend(long user1Id, long user2Id) {
        Person user1 = personRepository.findById(user1Id)
                .orElseThrow(NoSuchUserException::new);
        Person user2 = personRepository.findById(user2Id)
                .orElseThrow(NoSuchUserException::new);
        FriendList user1FriendList = user1.getProfile().getFriendList();
        user1FriendList.getFriends().add(user2.getProfile());
        FriendList user2FriendList = user2.getProfile().getFriendList();
        user2FriendList.getFriends().add(user1.getProfile());


        friendListRepository.save(user1FriendList);
        friendListRepository.save(user2FriendList);
    }

    /**
     * Creates a FriendRequest entity and assigns its sender value
     * as the Person entity for the sender, and receiver value with
     * Person entity of the receiver
     * @throws NoSuchUserException
     */
    public void sendFriendRequest(long senderId, long friendId) {
        Person sender = personRepository.findById(senderId)
                .orElseThrow(NoSuchUserException::new);
        Person receiver = personRepository.findById(friendId)
                .orElseThrow(NoSuchUserException::new);
        FriendRequest friendRequest = FriendRequest.builder()
                .status(FriendRequestStatus.RECEIVED)
                .dateTime(LocalDateTime.now())
                .sender(sender.getProfile())
                .receiver(receiver.getProfile())
                .build();
        friendRequestRepository.save(friendRequest);
    }

    /**
     * Adds the profile of user 1 to the second users FriendList entity value of friends
     * and adds the profile of user 2 to the first users FriendList entity.
     *
     * Changes the status of the FriendRequest to ACCEPTED
     * @param friendRequest
     */
    @Transactional
    public void acceptFriendRequest(FriendRequest friendRequest) {
        Profile user1 = friendRequest.getReceiver();
        Profile user2 = friendRequest.getSender();
        FriendList user1FriendList = user1.getFriendList();
        FriendList user2FriendList = user2.getFriendList();
        user1FriendList.getFriends().add(user2);
        user2FriendList.getFriends().add(user1);
        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);

        friendListRepository.save(user1FriendList);
        friendListRepository.save(user2FriendList);
        friendRequestRepository.save(friendRequest);
    }


    public void declineFriendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus(FriendRequestStatus.DECLINED);
        friendRequestRepository.save(friendRequest);
    }

    public void respondToFriendRequest(long friendRequestId, boolean status) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(NoFriendRequestException::new);
        if (status){
            acceptFriendRequest(friendRequest);
        }
        else{
            declineFriendRequest(friendRequest);
        }
    }


    public List<FriendRequestDto> getFriendRequestsForUser(long userId) {
        return friendRequestRepository.findAllByReceiverId(userId)
                .stream()
                .filter(request -> request.getStatus() == FriendRequestStatus.RECEIVED)
                .map(Mapper::mapFriendRequestToDto)
                .toList();

    }


    public FriendListDto getFriendsForUserId(long userId) {
        Person user = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        return Mapper.mapFriendListToDto(user.getProfile().getFriendList());
    }
    @Transactional
    public void removeFriend(long user1Id, long user2Id) {
        Person user1 = getPerson(user1Id);
        Person user2 = getPerson(user2Id);
        FriendList user1FriendList = user1.getProfile().getFriendList();
        FriendList user2FriendList = user2.getProfile().getFriendList();
        user1FriendList.getFriends().remove(user2.getProfile());
        user2FriendList.getFriends().remove(user1.getProfile());
        friendListRepository.save(user1FriendList);
        friendListRepository.save(user2FriendList);

    }
    private Person getPerson(long userId){
        return personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
    }
}
