package com.example.libraryapp.web;

import com.example.libraryapp.model.dto.ReviewDTO;
import com.example.libraryapp.model.dto.ReviewRequestDTO;
import com.example.libraryapp.service.ReviewService;
import com.example.libraryapp.utils.ExtractJWT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReviewDTO>> getAllByBookId(@RequestParam("bookId") Long bookId, Pageable pageable) {
        return ResponseEntity.ok(this.reviewService.getByBookId(bookId,pageable));
    }

    @PostMapping("/secure")
    public void  postReview(@RequestHeader(value = "Authorization") String token,
                            @RequestBody ReviewRequestDTO reviewRequestDTO) throws Exception {

    String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
    if (userEmail == null) {
        throw  new Exception("User email is missing");
    }
    reviewService.postReview(userEmail, reviewRequestDTO);
}

@GetMapping("/secure/user/book")
  public boolean reviewBookByUser(@RequestHeader(value = "Authorization") String token,
                                  @RequestParam Long bookId) throws Exception {
        String userEmail =ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if (userEmail == null) {
            throw  new Exception("User email is missing");
        }
        return reviewService.userReviewListed(userEmail, bookId);
}

}
