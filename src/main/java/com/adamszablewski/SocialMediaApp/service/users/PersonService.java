package com.adamszablewski.SocialMediaApp.service.users;


import com.adamszablewski.SocialMediaApp.dtos.PersonDto;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.exceptions.IncompleteDataException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.FriendListRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.utils.EntityUtils;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import com.adamszablewski.SocialMediaApp.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.adamszablewski.SocialMediaApp.enteties.TermsOfUse.ACCEPTED;

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


    public PersonDto getPerson(long userId) {
        return Mapper.mapPersonToDto(personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new));
    }
    public PersonDto getPerson(String email) {
        Person user =  personRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new);
        return Mapper.mapPersonToDto(user);

    }
    public void deleteUser(Long userId) {
        personRepository.deleteById(userId);

    }
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    @Transactional
    public void createUser(Person personData) {
        boolean valuesValidated = validator.validatePersonValues(personData);
        if (!valuesValidated){
            throw new IncompleteDataException();
        }
        String hashedPassword = hashPassword(personData.getPassword());
        Profile profile = createProfile();
        Person person = createPerson();
        FriendList friendList = createFriendlist();

        person.setBirthDate(personData.getBirthDate());
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

    private Person createPerson(){
        Person person = new Person();
        personRepository.save(person);
        return person;
    }
    private Profile createProfile(){
        Profile profile = new Profile();
        profileRepository.save(profile);
        return profile;
    }
    private FriendList createFriendlist(){
        FriendList friendList = new FriendList();
        friendListRepository.save(friendList);
        return friendList;
    }

    public long getUserIdForUsername(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new);
        return person.getId();
    }
    public String getHashedPassword(String username) {
        Person person = personRepository.findByEmail(username)
                .orElseThrow(NoSuchUserException::new);
        return person.getPassword();
    }
    public String getPhoneNumber(long userId) {
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        return person.getPhoneNumber();
    }
    public void resetPassword(String password, long userId) {
        Person person = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        person.setPassword(password);
        personRepository.save(person);
    }
}
