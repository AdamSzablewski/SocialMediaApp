package com.adamszablewski.SocialMediaApp.service.users;


import com.adamszablewski.SocialMediaApp.dtos.PersonDto;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.exceptions.IncompleteDataException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.utils.EntityUtils;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import com.adamszablewski.SocialMediaApp.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.adamszablewski.SocialMediaApp.enteties.TermsOfUse.ACCEPTED;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final EntityUtils entityUtils;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final ProfileRepository profileRepository;
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
    public void createUser(Person person) {
        boolean valuesValidated = validator.validatePersonValues(person);
        if (!valuesValidated){
            throw new IncompleteDataException();
        }
        String hashedPassword = hashPassword(person.getPassword());
        Profile profile = Profile.builder()
                .build();
        Person newPerson = Person.builder()
                .birthDate(person.getBirthDate())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .joinDate(LocalDateTime.now())
                .termsOfUse(ACCEPTED)
                .email(person.getEmail())
                .password(hashedPassword)
                .profile(profile)
                .build();
        FriendList friendList = FriendList.builder()
                .user(newPerson)
                .build();
        profile.setFriendList(friendList);
        personRepository.save(newPerson);



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
