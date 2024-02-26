package com.adamszablewski.SocialMediaApp.enteties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JWT {
    private String token;
    private long userId;
}
