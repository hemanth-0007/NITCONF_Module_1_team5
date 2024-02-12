package com.nitconfbackend.nitconf.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Review
 * Represents a review in the system.
 * @since 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="reviews")
public class Review {
    /**
     * Unique identifier for the review.
     */
    @Id public String id;
    /**
     * User who provided the review.
     */
    @DBRef
    public User reviewer;
     /**
     * Comment given in the review.
     */
    public String comment;
}
