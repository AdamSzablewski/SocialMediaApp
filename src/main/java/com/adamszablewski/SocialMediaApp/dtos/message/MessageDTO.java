package com.adamszablewski.SocialMediaApp.dtos.message;

import com.adamszablewski.SocialMediaApp.dtos.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    private long id;
    private Set<Long> receivers;
    private ProfileDto sender;
    private LocalDateTime dateTime;
    private String message;
}
