package com.adamszablewski.SocialMediaApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class PersonDto {

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate birthDate;
}
