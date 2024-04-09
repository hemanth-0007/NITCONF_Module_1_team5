package com.nitconfbackend.nitconf.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.nitconfbackend.nitconf.models.Review;


public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<Review> findById(String id);
}
