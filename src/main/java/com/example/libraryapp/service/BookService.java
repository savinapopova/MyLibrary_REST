package com.example.libraryapp.service;

import com.example.libraryapp.model.dto.CarouselBookDTO;
import com.example.libraryapp.model.dto.SearchBookDTO;
import com.example.libraryapp.model.entity.Book;
import com.example.libraryapp.model.entity.Checkout;
import com.example.libraryapp.model.entity.History;
import com.example.libraryapp.repository.BookRepository;
import com.example.libraryapp.repository.CheckoutRepository;
import com.example.libraryapp.repository.HistoryRepository;
import com.example.libraryapp.responseModels.ShelfCurrentLoansResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {
    private BookRepository bookRepository;
    private CheckoutRepository checkoutRepository;
    private ModelMapper modelMapper;

    private HistoryRepository historyRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository,
                       ModelMapper modelMapper, HistoryRepository historyRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.modelMapper = modelMapper;
        this.historyRepository = historyRepository;
    }

    public List<CarouselBookDTO> getCarouselBooks() {
        List<Book> books = this.bookRepository.findAll();
        return books.stream()
                .map(book -> this.modelMapper.map(book, CarouselBookDTO.class))
                .limit(9).toList();
    }

    public Page<SearchBookDTO> getSearchedBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return bookPage.map(book -> this.modelMapper.map(book, SearchBookDTO.class));
    }

    public Page<SearchBookDTO> getBooksByTitle(String title, Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAllByTitleContaining(title, pageable);
        return bookPage.map(book -> this.modelMapper.map(book, SearchBookDTO.class));
    }

    public Page<SearchBookDTO> getBooksByCategory(String category, Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAllByCategory(category, pageable);
        return bookPage.map(book -> this.modelMapper.map(book, SearchBookDTO.class));
    }

    public SearchBookDTO getSingleBook(Long bookId) {
        return this.bookRepository.findById(bookId)
                .map(book -> this.modelMapper.map(book, SearchBookDTO.class))
                .orElse(null);
    }

    public SearchBookDTO checkoutBook(Long bookId, String userEmail) throws Exception {
        Optional<Book> optionalBook = this.bookRepository.findById(bookId);

        Checkout validateCheckout = this.checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (optionalBook.isEmpty() || validateCheckout != null || optionalBook.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or already checked out");
        }

        Book book = optionalBook.get();
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookRepository.save(book);

        Checkout checkout = new Checkout(userEmail, LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(), book.getId());

        checkoutRepository.save(checkout);

        return this.modelMapper.map(book, SearchBookDTO.class);

    }

    public boolean checkoutBookByUser(Long bookId, String userEmail) {
        Checkout validateCheckout = this.checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (validateCheckout != null) {
            return true;
        } else {
            return false;
        }
    }

    public int currentLoansCount(String userEmail) {
        List<Checkout> checkouts = this.checkoutRepository.findBooksByUserEmail(userEmail);
        return checkouts.size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);

        List<Long> bookIdList = new ArrayList<>();

        for (Checkout checkout : checkoutList) {
            bookIdList.add(checkout.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books) {

            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId() == book.getId())
                    .findFirst();

            if (checkout.isPresent()) {
                Date returnDate = sdf.parse(checkout.get().getReturnDate());
                Date nowDate = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long timeDifference = time.convert(returnDate.getTime() - nowDate.getTime(),
                        TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses
                        .add(new ShelfCurrentLoansResponse(book, (int) timeDifference));
            }
        }

        return shelfCurrentLoansResponses;
    }

    public void returnBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (book.isEmpty() || validateCheckout == null) {
            throw  new Exception("Book does not exist or checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());
        checkoutRepository.deleteById(validateCheckout.getId());

        History history = new History(userEmail, validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(), book.get().getTitle(), book.get().getAuthor(),
                book.get().getDescription(), book.get().getImage());

        historyRepository.save(history);
    }

    public void renewLoan(String userEmail, Long bookId) throws Exception {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (validateCheckout == null) {
            throw  new Exception("Book does not exist or checked out by user");
        }
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");

        Date returnDate = sdf.parse(validateCheckout.getReturnDate());
        Date nowDate = sdf.parse(LocalDate.now().toString());

        TimeUnit time = TimeUnit.DAYS;

        if (returnDate.compareTo(nowDate) > 0 || returnDate.compareTo(nowDate) == 0) {
          validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
          checkoutRepository.save(validateCheckout);
        }
    }
}
