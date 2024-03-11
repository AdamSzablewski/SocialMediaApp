package com.adamszablewski.SocialMediaApp.utils;

import com.adamszablewski.SocialMediaApp.enteties.Person;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PersonManager {

    private final PersonRepository personRepository;

    public Person getPerson(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new);


    }

}
