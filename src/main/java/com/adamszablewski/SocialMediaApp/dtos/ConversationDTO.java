package com.adamszablewski.SocialMediaApp.dtos;


import com.adamszablewski.SocialMediaApp.dtos.message.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConversationDTO {

    private long id;
    private long ownerId;
    private List<ProfileDto> participants;
    private List<MessageDTO> messages = new ArrayList<>();

}
