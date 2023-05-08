package com.ercanbeyen.casestudy.repository;

import com.ercanbeyen.casestudy.entity.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository {
    Long count();
    List<Movie> findAll();
    Optional<Movie> findById(String id);
    void save(Movie movie);
    void deleteById(String id);
    boolean existsById(String id);

}
