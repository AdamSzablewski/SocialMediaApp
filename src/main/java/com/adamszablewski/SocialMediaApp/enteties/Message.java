package com.adamszablewski.SocialMediaApp.enteties;

import com.adamszablewski.SocialMediaApp.enteties.friends.Profile;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Image;
import com.adamszablewski.SocialMediaApp.enteties.multimedia.Video;
import com.adamszablewski.SocialMediaApp.interfaces.Identifiable;
import com.adamszablewski.SocialMediaApp.utils.EncryptionUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Message implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne
    @JsonIgnoreProperties("messages")
    private Conversation conversation;
    private String text;
    @ManyToOne
    private Profile sender;
    @OneToOne
    private Image image;
    @OneToOne
    private Video video;
    private LocalDateTime dateTime;

    public void setEncryptedMessage(String text) {
        try {
            this.text = EncryptionUtil.encryptText(text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getDecryptedMessage(){
        try {
            return EncryptionUtil.decryptText(this.text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String getText(){
        try {
            return EncryptionUtil.decryptText(this.text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
