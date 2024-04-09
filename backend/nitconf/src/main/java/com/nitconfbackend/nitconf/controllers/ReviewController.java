package com.nitconfbackend.nitconf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nitconfbackend.nitconf.models.DocumentVersion;
import com.nitconfbackend.nitconf.models.Review;
import com.nitconfbackend.nitconf.repositories.DocumentVersionRepository;
import com.nitconfbackend.nitconf.repositories.ReviewRepository;
 

import java.util.List;
 



@RestController

@RequestMapping("/api/review")
public class ReviewController{

    @Autowired
    private DocumentVersionRepository documentRepo;

    @Autowired
    private ReviewRepository reviewRepo;


   /**
     * Retrieves a list of reviews for a specific document.
     *
     * @param id The ID of the document.
     * @return ResponseEntity containing the list of reviews for the specified document.
     * @throws java.util.NoSuchElementException if the document with the given ID is not found.
     */
    @GetMapping("/doc/{id}")
    public ResponseEntity<List<Review>> getReviewList(@PathVariable String id) {
        if (id == null)
            return ResponseEntity.notFound().build();
        DocumentVersion doc = documentRepo.findById(id).orElseThrow();
        return ResponseEntity.ok(doc.getReviews());
    }
     /**
     * Retrieves a specific review by its ID.
     *
     * @param id The ID of the review.
     * @return ResponseEntity containing the requested review.
     * @throws java.util.NoSuchElementException if the review with the given ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable String id) {
        if (id == null)
            return ResponseEntity.notFound().build();
        Review review = reviewRepo.findById(id).orElseThrow();
        return ResponseEntity.ok(review);
    }
    
}