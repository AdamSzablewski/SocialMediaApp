package com.adamszablewski.SocialMediaApp.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConversationDTO {

    private long id;
    private long ownerId;
    private Set<Long> participants;
    private List<MessageDTO> messages;

}
