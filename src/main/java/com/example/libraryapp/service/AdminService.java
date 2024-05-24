package com.example.libraryapp.service;

import com.example.libraryapp.model.dto.AddBookDTO;
import com.example.libraryapp.model.entity.Book;
import com.example.libraryapp.repository.BookRepository;
import com.example.libraryapp.repository.CheckoutRepository;
import com.example.libraryapp.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService {

    private BookRepository bookRepository;

    private ReviewRepository reviewRepository;

    private CheckoutRepository checkoutRepository;
    private ModelMapper modelMapper;

    public AdminService(BookRepository bookRepository, ReviewRepository reviewRepository,
                        CheckoutRepository checkoutRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.checkoutRepository = checkoutRepository;
        this.modelMapper = modelMapper;
    }

    public void increaseBookQuantity(Long bookId) throws Exception{
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new Exception("Book not found");
        }
        Book book = optionalBook.get();
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        book.setCopies(book.getCopies() + 1);
        bookRepository.save(book);

    }

    public void decreaseBookQuantity(Long bookId) throws Exception{
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty() || optionalBook.get().getCopiesAvailable() <= 0 ||
                optionalBook.get().getCopies() <= 0) {
            throw new Exception("Book not found or quantity is locked");
        }
        Book book = optionalBook.get();
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        book.setCopies(book.getCopies() - 1);
        bookRepository.save(book);

    }

    public void postBook(AddBookDTO addBookDTO) {

        Book book = modelMapper.map(addBookDTO, Book.class);
        book.setCopiesAvailable(addBookDTO.getCopies());
        this.bookRepository.save(book);
    }

    public void deleteBook(Long bookId) throws Exception{
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new Exception("Book not found");
        }
        Book book = optionalBook.get();
        bookRepository.delete(book);
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }
}
