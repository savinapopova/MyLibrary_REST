package com.example.libraryapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "messages")
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String question;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "admin_email")
    private String adminEmail;

    private String response;

    private boolean closed;





    public Message(String title, String question) {
        this.title = title;
        this.question = question;
    }

    public Message() {
    }
}
