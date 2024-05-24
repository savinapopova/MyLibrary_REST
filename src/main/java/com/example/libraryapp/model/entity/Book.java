package com.example.libraryapp.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private String description;

    private int copies;

    @Column(name = "copies_available")
    private int copiesAvailable;

    private String category;

    @Column(name = "img")
    private String image;

    public Book() {
    }
}
