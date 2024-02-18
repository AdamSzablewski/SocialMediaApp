package com.adamszablewski.SocialMediaApp.dtos;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    private String phoneNumber;
}
