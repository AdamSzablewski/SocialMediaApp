package com.adamszablewski.SocialMediaApp.repository;

import com.adamszablewski.SocialMediaApp.enteties.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);

    void deleteByEmail(String userEmail);

    boolean existsByEmail(String email);

    @Query(value = "Select * from person p where p.first_name LIKE :firstName% and p.last_name LIKE :lastName%", nativeQuery = true )
    List<Person> getUsersThatMatch(String firstName, String lastName);
    @Query(value = "Select * from person p where p.first_name LIKE :firstName%", nativeQuery = true )
    List<Person> getUsersByFirstName(String firstName);
    @Query(value = "Select * from person p where p.last_name LIKE :lastName%", nativeQuery = true )
    List<Person> getUsersByLastName(String lastName);
}
