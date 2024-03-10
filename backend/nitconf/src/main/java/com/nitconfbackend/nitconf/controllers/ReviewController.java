package com.nitconfbackend.nitconf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nitconfbackend.nitconf.models.DocumentVersion;
import com.nitconfbackend.nitconf.models.Review;
// import com.nitconfbackend.nitconf.models.Role;
import com.nitconfbackend.nitconf.models.User;
import com.nitconfbackend.nitconf.repositories.DocumentVersionRepository;
import com.nitconfbackend.nitconf.repositories.ReviewRepository;
import com.nitconfbackend.nitconf.repositories.UserRepository;
import com.nitconfbackend.nitconf.types.ReviewRequest;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController

@RequestMapping("/api/review")
public class ReviewController{

    @Autowired
    private DocumentVersionRepository documentRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private UserRepository userRepo;

   /**
     * Retrieves a list of reviews for a specific document.
     *
     * @param id The ID of the document.
     * @return ResponseEntity containing the list of reviews for the specified document.
     * @throws java.util.NoSuchElementException if the document with the given ID is not found.
     */
    @GetMapping("/doc/{id}")
    public ResponseEntity<List<Review>> getMethodName(@PathVariable String id) {
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

     /**
     * Creates a new review for a specific document.
     *
     * @param body The request body containing the review details.
     * @param id   The ID of the document for which the review is created.
     * @return ResponseEntity indicating the success or failure of the review creation.
     * @throws java.util.NoSuchElementException if the document with the given ID is not found.
     * @throws IllegalArgumentException        if the user does not have the required role for creating a review.
     */
    
    @PostMapping("/{id}")
    public ResponseEntity<String> createReview(@RequestBody ReviewRequest body, @PathVariable String id) {
        Review review = new Review();
        review.setComment(body.comment);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User profile = userRepo.findByEmail(email).orElseThrow();
        if (id == null)
            return ResponseEntity.notFound().build();
        // if (profile.role != Role.REVIEWER && profile.role != Role.PROGRAM_COMMITTEE) {
        //     return ResponseEntity.badRequest().build();
        // }
        DocumentVersion targetDoc = documentRepo.findById(id).orElseThrow() ;
        // review.doc=docu;
        review.setReviewer(profile);
        reviewRepo.save(review);
        targetDoc.getReviews().add(review);
        documentRepo.save(targetDoc);
        return ResponseEntity.ok("Review created");
    }
    
}