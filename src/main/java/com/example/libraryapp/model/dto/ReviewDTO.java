package com.example.libraryapp.model.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
public class ReviewDTO {

    private Long id;

    private String userEmail;


    private Date date;

    private double rating;


    private Long bookId;


    private String reviewDescription;

    public ReviewDTO() {
    }
}
