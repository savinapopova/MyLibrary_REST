package com.example.libraryapp.model.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class ReviewRequestDTO {

    private double rating;

    private Long bookId;

    private Optional<String> reviewDescription;

    public ReviewRequestDTO() {
    }
}
