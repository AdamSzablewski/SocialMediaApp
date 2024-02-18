package com.adamszablewski.SocialMediaApp.repository.multimedia;

import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.users.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepositroy extends JpaRepository<Image, Long> {



}
