package com.example.libraryapp.model.dto;

import com.example.libraryapp.model.entity.Book;
import lombok.Data;

@Data
public class CarouselBookDTO {

    private Long id;

    private String title;

    private String author;

    private String image;

    public CarouselBookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.image = book.getImage();
    }

    public CarouselBookDTO() {
    }
}
