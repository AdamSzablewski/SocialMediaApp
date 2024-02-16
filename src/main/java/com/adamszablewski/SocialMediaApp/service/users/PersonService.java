package com.adamszablewski.SocialMediaApp.service.users;


import com.adamszablewski.SocialMediaApp.dtos.PersonDto;
import com.adamszablewski.SocialMediaApp.enteties.users.Person;
import com.adamszablewski.SocialMediaApp.exceptions.IncompleteDataException;
import com.adamszablewski.SocialMediaApp.exceptions.NoSuchUserException;
import com.adamszablewski.SocialMediaApp.repository.PersonRepository;
import com.adamszablewski.SocialMediaApp.utils.EntityUtils;
import com.adamszablewski.SocialMediaApp.utils.UniqueIdGenerator;
import com.adamszablewski.SocialMediaApp.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final EntityUtils entityUtils;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final Validator validator;


    public PersonDto getPerson(long userId) {
        return entityUtils.mapPersonToDto(personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new));
    }
    public PersonDto getPerson(String email) {
        return entityUtils.mapPersonToDto(personRepository.findByEmail(email)
                .orElseThrow(NoSuchUserException::new));
    }
    public void deleteUser(Long userId) {
        personRepository.deleteById(userId);

    }
    public void createUser(Person person) {
        boolean valuesValidated = validator.validatePersonValues(person);
        if (!valuesValidated){
            throw new IncompleteDataException();
        }
        String hashedPassword = person.getPassword();
        Person newPerson = Person.builder()
                .birthDate(person.getBirthDate())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .username(person.getUsername())
                .email(person.getEmail())
                .password(hashedPassword)
                .build();
        personRepository.save(newPerson);
    }
    public String getUsernameFromId(long userId) {
        Person user = personRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
        return user.getUsername();
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
