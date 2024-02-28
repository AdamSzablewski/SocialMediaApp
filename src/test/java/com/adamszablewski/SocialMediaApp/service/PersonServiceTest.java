package com.adamszablewski.SocialMediaApp.service;

import com.adamszablewski.SocialMediaApp.dtos.PersonDto;
import com.adamszablewski.SocialMediaApp.dtos.RegisterDto;
import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.enteties.friends.FriendList;
import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.FriendListRepository;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.repository.posts.ProfileRepository;
import com.adamszablewski.SocialMediaApp.security.SecurityService;
import com.adamszablewski.SocialMediaApp.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private FriendListRepository friendListRepository;
    @Mock
    private SecurityService securityService;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteUserTest_shouldDeleteUser(){
        long userId = 1L;

        personService.deleteUser(userId);

        verify(personRepository).deleteById(userId);
    }

    @Test
    void createUserTest_shouldCreateUser(){
        String hashedPassword = "HASHED";
        RegisterDto personData = RegisterDto.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("password")
                .build();
        FriendList friendList = new FriendList();
        Profile profile = new Profile();
        profile.setFriendList(friendList);

        Person person = Person.builder()
                .firstName(personData.getFirstName())
                .lastName(personData.getLastName())
                .phoneNumber(personData.getPhoneNumber())
                .email(personData.getEmail())
                .joinDate(LocalDateTime.now())
                .profile(profile)
                .password(hashedPassword)
                .build();
        profile.setUser(person);
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        when(securityService.hashPassword(personData.getPassword())).thenReturn(hashedPassword);

        personService.createUser(personData);
        verify(personRepository, times(2)).save(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue().getFirstName()).isEqualTo(person.getFirstName());
        assertThat(personArgumentCaptor.getValue().getLastName()).isEqualTo(person.getLastName());
        assertThat(personArgumentCaptor.getValue().getPhoneNumber()).isEqualTo(person.getPhoneNumber());
        assertThat(personArgumentCaptor.getValue().getEmail()).isEqualTo(person.getEmail());
        assertThat(personArgumentCaptor.getValue().getJoinDate()).isEqualToIgnoringSeconds(person.getJoinDate());
        assertThat(personArgumentCaptor.getValue().getProfile().getUser().getId()).isEqualTo(person.getProfile().getUser().getId());
        assertThat(personArgumentCaptor.getValue().getPassword()).isEqualTo(person.getPassword());


        verify(friendListRepository, times(2)).save(any(FriendList.class));
        verify(profileRepository, times(2)).save(any(Profile.class));
    }
    @Test
    void createPersonTest_shouldCreatePerson(){
        Person result = personService.createPerson();
        verify(personRepository).save(eq(new Person()));
        assertThat(result).isEqualTo(new Person());
    }
    @Test
    void createProfileTest_shouldCreateProfile(){
        Profile result = personService.createProfile();
        verify(profileRepository).save(eq(new Profile()));
        assertThat(result).isEqualTo(new Profile());
    }
    @Test
    void createFriendlistTest_shouldCreateFriendlist(){
        FriendList result = personService.createFriendlist();
        verify(friendListRepository).save(eq(new FriendList()));
        assertThat(result).isEqualTo(new FriendList());
    }
    @Test
    void getUserIdForUsernameTest_ShouldReturnId(){
        String userEmail = "test@example.com";
        Person person = Person.builder()
                .id(1L)
                .email(userEmail)
                .build();
        when(personRepository.findByEmail(userEmail)).thenReturn(Optional.of(person));

        long result = personService.getUserIdByEmail(userEmail);

        assertThat(result).isEqualTo(person.getId());
    }
    @Test
    void getUserIdForUsernameTest_ShouldThrowNoSuchUserException(){
        String userEmail = "test@example.com";
        when(personRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, ()->{
            personService.getUserIdByEmail(userEmail);
        });
    }
    @Test
    void resetPasswordTest_ShouldReturnId(){
        long userId = 1L;
        String newPassword = "newPassword";
        String password = "password";
        Person person = Person.builder()
                .id(userId)
                .password(password)
                .build();
        when(personRepository.findById(userId)).thenReturn(Optional.of(person));

        personService.resetPassword(newPassword, userId);

        verify(personRepository).save(any());
        assertThat(person.getPassword()).isEqualTo(newPassword);
    }
    @Test
    void resetPasswordTest_ThrowsNoSuchUserException(){
        String userEmail = "test@example.com";
        when(personRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, ()->{
            personService.getUserIdByEmail(userEmail);
        });
    }

}
