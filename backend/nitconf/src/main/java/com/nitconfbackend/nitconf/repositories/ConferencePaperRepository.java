package com.nitconfbackend.nitconf.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.nitconfbackend.nitconf.models.ConferencePaper;


public interface ConferencePaperRepository extends MongoRepository<ConferencePaper, String> {
}
