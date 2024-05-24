package com.example.libraryapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "histories")
@Data
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "checkout_date")
    private String checkoutDate;

    @Column(name = "return_date")
    private String returnDate;

    private String title;

    private String author;

    private String description;

    private String image;

    public History(String userEmail, String checkoutDate, String returnDate,
                   String title, String author, String description, String image) {
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
        this.title = title;
        this.author = author;
        this.description = description;
        this.image = image;
    }

    public History() {
    }
}
