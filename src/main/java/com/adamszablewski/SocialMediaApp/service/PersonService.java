package com.adamszablewski.SocialMediaApp.service;


import com.adamszablewski.SocialMediaApp.dtos.PersonDto;
import com.adamszablewski.SocialMediaApp.dtos.RegisterDto;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.FriendListRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.security.SecurityService;
import com.adamszablewski.SocialMediaApp.utils.EntityUtils;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import com.adamszablewski.SocialMediaApp.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final EntityUtils entityUtils;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final ProfileRepository profileRepository;
    private final FriendListRepository friendListRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;
    private final SecurityService securityService;


    public PersonDto getPerson(long userId) {
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        return Mapper.mapPersonToDto(person);
    }
    public PersonDto getPerson(String email) {
        Person user =  personRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new);
        return Mapper.mapPersonToDto(user);

    }
    public void deleteUser(Long userId) {
        personRepository.deleteById(userId);

    }

    /**
     * Creates and saves a Person entity and fills it values from the RegisterDto.
     * Creates associated Profile and FriendList entity and sets the correct
     * relationship between the three entities.
     * @param personData
     * @Returns void
     */
    @Transactional
    public void createUser(RegisterDto personData) {

        String hashedPassword = securityService.hashPassword(personData.getPassword());
        Profile profile = createProfile();
        Person person = createPerson();
        FriendList friendList = createFriendlist();

        person.setFirstName(personData.getFirstName());
        person.setLastName(personData.getLastName());
        person.setPhoneNumber(personData.getPhoneNumber());
        person.setEmail(personData.getEmail());
        person.setJoinDate(LocalDateTime.now());
        person.setProfile(profile);
        person.setPassword(hashedPassword);

        friendList.setUser(person);
        profile.setUser(person);
        profile.setFriendList(friendList);
        personRepository.save(person);
        friendListRepository.save(friendList);
        profileRepository.save(profile);
    }

    public Person createPerson(){
        Person person = new Person();
        personRepository.save(person);
        return person;
    }
    public Profile createProfile(){
        Profile profile = new Profile();
        profileRepository.save(profile);
        return profile;
    }
    public FriendList createFriendlist(){
        FriendList friendList = new FriendList();
        friendListRepository.save(friendList);
        return friendList;
    }

    public long getUserIdByEmail(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new);
        return person.getId();
    }

    public void resetPassword(String password, long userId) {
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        person.setPassword(password);
        personRepository.save(person);
    }
}
