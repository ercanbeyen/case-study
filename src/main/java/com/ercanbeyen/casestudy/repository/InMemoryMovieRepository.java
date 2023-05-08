package com.ercanbeyen.casestudy.repository;

import com.ercanbeyen.casestudy.entity.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InMemoryMovieRepository {
    Long count();
    List<Movie> findAll();
    Optional<Movie> findById(String id);
    Movie save(Movie movie);
    String deleteById(String id);
    boolean existsById(String id);

}
