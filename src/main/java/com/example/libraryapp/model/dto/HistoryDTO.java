package com.example.libraryapp.model.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class HistoryDTO {

    private Long id;


    private String userEmail;


    private String checkoutDate;


    private String returnDate;

    private String title;

    private String author;

    private String description;

    private String image;

    public HistoryDTO(String userEmail, String checkoutDate, String returnDate,
                      String title, String author, String description, String image) {
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
        this.title = title;
        this.author = author;
        this.description = description;
        this.image = image;
    }

    public HistoryDTO() {
    }
}
