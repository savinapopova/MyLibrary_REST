package com.example.libraryapp.web;

import com.example.libraryapp.model.dto.CarouselBookDTO;
import com.example.libraryapp.model.dto.SearchBookDTO;
import com.example.libraryapp.model.entity.Book;
import com.example.libraryapp.responseModels.ShelfCurrentLoansResponse;
import com.example.libraryapp.service.BookService;
import com.example.libraryapp.utils.ExtractJWT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/carousel")
    public ResponseEntity<List<CarouselBookDTO>> getCarouselBooks() {
        return ResponseEntity.ok(this.bookService.getCarouselBooks());
    }

    @GetMapping()
    public ResponseEntity<Page<SearchBookDTO>> getSearchedBooks(Pageable pageable) {
        Page<SearchBookDTO> books = bookService.getSearchedBooks(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/findByTitleContaining")
    public ResponseEntity<Page<SearchBookDTO>> getByTitle(@RequestParam("title") String title,
                                                          Pageable pageable) {
        Page<SearchBookDTO> books = bookService.getBooksByTitle(title, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/findByCategory")
    public ResponseEntity<Page<SearchBookDTO>> getByCategory(@RequestParam("category") String category,
                                                          Pageable pageable) {
        Page<SearchBookDTO> books = bookService.getBooksByCategory(category, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<SearchBookDTO> getSingleBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(this.bookService.getSingleBook(bookId));
    }

    @PutMapping("/secure/checkout")
    public ResponseEntity<SearchBookDTO> checkoutBook (@RequestHeader(value = "Authorization") String token,
            @RequestParam("bookId") Long bookId) throws Exception {
       String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return ResponseEntity.ok(this.bookService.checkoutBook(bookId, userEmail));
    }

    @GetMapping("/secure/isCheckedOut/byUser")
    public ResponseEntity<Boolean> isCheckedOutByUser(@RequestHeader(value = "Authorization") String token,
            @RequestParam("bookId") Long bookId) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return ResponseEntity.ok(this.bookService.checkoutBookByUser(bookId, userEmail));
    }

    @GetMapping("/secure/currentLoans/count")
    ResponseEntity<Integer> getCurrentLoansCount(@RequestHeader(value = "Authorization") String token) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return ResponseEntity.ok(this.bookService.currentLoansCount(userEmail));
    }

    @GetMapping("/secure/currentLoans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization")
                                                        String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoans(userEmail);
    }

    @PutMapping("/secure/return")
    public void  returnBook(@RequestHeader(value = "Authorization") String token,
                            @RequestParam Long bookId)
            throws Exception {

        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        bookService.returnBook(userEmail, bookId);

    }
    @PutMapping("/secure/renew/loan")
    public void  renewLoan(@RequestHeader(value = "Authorization") String token,
                            @RequestParam Long bookId)
            throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        bookService.renewLoan(userEmail, bookId);
    }
}
