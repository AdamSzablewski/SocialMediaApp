package com.adamszablewski.SocialMediaApp.repository;

import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepositroy extends JpaRepository<Image, Long> {



}
