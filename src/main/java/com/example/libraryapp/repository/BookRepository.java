package com.example.libraryapp.repository;

import com.example.libraryapp.model.entity.Book;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByTitleContaining(String title, Pageable pageable);

    Page<Book> findAllByCategory(String category, Pageable pageable);

    @Query("SELECT  o FROM Book  o WHERE  id IN :book_ids")
    List<Book> findBooksByBookIds(@Param("book_ids") List<Long> bookId);

//    Page<Book>findByTitleContaining(@RequestParam("title") String title,
//            Pageable pageable);

//    Page<Book>findByCategory(@RequestParam("category") String category,
//            Pageable pageable);

}
