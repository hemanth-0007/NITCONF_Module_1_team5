package com.nitconfbackend.nitconf.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.nitconfbackend.nitconf.models.DocumentVersion;
import com.nitconfbackend.nitconf.models.Review;
import com.nitconfbackend.nitconf.repositories.DocumentVersionRepository;
import com.nitconfbackend.nitconf.repositories.ReviewRepository;
import com.nitconfbackend.nitconf.repositories.UserRepository;

public class ReviewControllerTest {
      @Mock
    private Authentication authentication;

    @Mock
    private DocumentVersionRepository documentVersionRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewController reviewController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGetReviewsDocumentVersion(){
        String docId = "1";
        List<Review> reviews = new ArrayList<>();

        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setReviews(reviews);

        when(documentVersionRepository.findById(docId)).thenReturn(Optional.of(documentVersion));

        ResponseEntity<List<Review>> responseEntity = reviewController.getReviewList(docId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void testGetReviewsDocumentVersionWithNullId(){
        String docId = null;

        ResponseEntity<List<Review>> responseEntity = reviewController.getReviewList(docId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        // assertThrows(Exception.class, () -> {
        //     reviewController.getReviewList(docId);
        // });

    }

    @Test
    void testGetReviewsDocumentVersionWithInvalidId(){
        String docId = "1";

        when(documentVersionRepository.findById(docId)).thenThrow(new NoSuchElementException());

        // ResponseEntity<List<Review>> responseEntity = reviewController.getReviewList(docId);

        // assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertThrows(NoSuchElementException.class, () -> {
            reviewController.getReviewList(docId);
        });
    }

    @Test
    void testGetReview(){
        String reviewId = "1";
        Review review = new Review();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        ResponseEntity<Review> responseEntity = reviewController.getReview(reviewId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void testGetReviewWithNullId(){
        String reviewId = null;

        ResponseEntity<Review> responseEntity = reviewController.getReview(reviewId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    void testGetReviewWithInvalidId(){
        String reviewId = "1";

        when(reviewRepository.findById(reviewId)).thenThrow(new NoSuchElementException());

        // ResponseEntity<Review> responseEntity = reviewController.getReview(reviewId);

        // assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertThrows(NoSuchElementException.class, () -> {
            reviewController.getReview(reviewId);
        });

    }

  
}
