package com.ercanbeyen.casestudy.repository;

import com.ercanbeyen.casestudy.document.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {

}
