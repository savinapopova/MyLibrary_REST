package com.example.libraryapp.service;

import com.example.libraryapp.model.dto.ReviewDTO;
import com.example.libraryapp.model.dto.ReviewRequestDTO;
import com.example.libraryapp.model.entity.Review;
import com.example.libraryapp.repository.BookRepository;
import com.example.libraryapp.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;

    private BookRepository bookRepository;
    private ModelMapper modelMapper;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    public Page<ReviewDTO> getByBookId(Long bookId, Pageable pageable) {
        return this.reviewRepository.findByBookId(bookId, pageable)
                .map(review -> this.modelMapper.map(review, ReviewDTO.class));
    }

    public void postReview(String userEmail, ReviewRequestDTO reviewRequestDTO) throws Exception {

        Optional<Review> validateReview = this.reviewRepository
                .findByUserEmailAndBookId(userEmail, reviewRequestDTO.getBookId());

        if (validateReview.isPresent()) {
            throw new Exception("Review already created");
        }

        Review review = modelMapper.map(reviewRequestDTO, Review.class);
        review.setUserEmail(userEmail);
        if (reviewRequestDTO.getReviewDescription().isPresent()) {
            review.setReviewDescription(reviewRequestDTO.getReviewDescription()
                    .map(Object::toString)
                    .orElse(null));
        }
        review.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(review);
    }

    public boolean userReviewListed(String userEmail, Long bookId) {
        Optional<Review> optionalReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (optionalReview.isPresent()) {
            return true;
        }
        return false;
    }
}
