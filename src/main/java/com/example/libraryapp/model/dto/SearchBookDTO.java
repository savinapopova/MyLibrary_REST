package com.example.libraryapp.model.dto;

import com.example.libraryapp.model.entity.Book;
import lombok.Data;

@Data
public class SearchBookDTO {

    private Long id;

    private String title;

    private String author;

    private String image;

    private String description;

    private int copies;

    private int copiesAvailable;

    public SearchBookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.image = book.getImage();
        this.description = book.getDescription();
        this.copies = book.getCopies();
        this.copiesAvailable = book.getCopiesAvailable();
    }

    public SearchBookDTO() {
    }
}
