package com.ercanbeyen.casestudy.repository;

import com.ercanbeyen.casestudy.document.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findAllByTitleLikeIgnoreCase(String title);
}
